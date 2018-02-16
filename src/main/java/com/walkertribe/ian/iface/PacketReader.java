package com.walkertribe.ian.iface;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.UnknownPacket;
import com.walkertribe.ian.protocol.UnparsedPacket;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.setup.VersionPacket;
import com.walkertribe.ian.protocol.core.world.ObjectUpdatePacket;
import com.walkertribe.ian.util.BitField;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.ByteArrayReader;
import com.walkertribe.ian.util.TextUtil;
import com.walkertribe.ian.util.Version;

/**
 * Facilitates reading packets from an InputStream. This object may be reused to
 * read as many packets as desired from a single InputStream. Individual packet
 * classes can read their properties by using the read*() methods on this class.
 * @author rjwut
 */
public class PacketReader {
	private static final Set<Integer> REQUIRED_PACKET_TYPES = new HashSet<Integer>();

	static {
		REQUIRED_PACKET_TYPES.add(CorePacketType.PLAIN_TEXT_GREETING.getHash());
		REQUIRED_PACKET_TYPES.add(CorePacketType.CONNECTED.getHash());
		REQUIRED_PACKET_TYPES.add(CorePacketType.HEARTBEAT.getHash());
	}

	private Context ctx;
	private ConnectionType connType;
	private InputStream in;
	private byte[] intBuffer = new byte[4];
	private boolean parse = true;
	private PacketFactoryRegistry factoryRegistry;
	private ListenerRegistry listenerRegistry;
	private Version version;
	private ByteArrayReader payload;
	private SortedMap<String, byte[]> unknownProps;
	private ObjectType objectType;
	private int objectId;
	private BitField bitField;
	private SortedMap<String, byte[]> unknownObjectProps;

	/**
	 * Wraps the given InputStream with this PacketReader.
	 */
	public PacketReader(Context ctx, ConnectionType connType, InputStream in,
			PacketFactoryRegistry factoryRegistry,
			ListenerRegistry listenerRegistry) {
		this.ctx = ctx;
		this.connType = connType;
		this.in = in;
		this.factoryRegistry = factoryRegistry;
		this.listenerRegistry = listenerRegistry;
	}

	/**
	 * Returns the Context associated with this PacketReader.
	 */
	public Context getContext() {
		return ctx;
	}

	/**
	 * If set to false, all packets will be returned as UnknownPackets. This is
	 * useful for testing purposes to easily capture packet payloads in their
	 * raw form without bothering to parse any of them. By default, this
	 * property is true, meaning that all known packets will be parsed.
	 */
	public void setParsePackets(boolean parse) {
		this.parse = parse;
	}

	/**
	 * Returns the server Version, or null if unknown.
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * Reads a single packet and returns it. The given Debugger will also be
	 * notified.
	 */
	public ParseResult readPacket(Debugger debugger) throws ArtemisPacketException {
		objectType = null;
		objectId = 0;
		bitField = null;
		unknownProps = new TreeMap<String, byte[]>();
		unknownObjectProps = new TreeMap<String, byte[]>();

		// header (0xdeadbeef)
		final int header = readIntFromStream();

		if (header != ArtemisPacket.HEADER) {
			throw new ArtemisPacketException(
					"Illegal packet header: " + Integer.toHexString(header)
			);
		}

		// packet length
		final int len = readIntFromStream();

		if (len <= 8) {
			throw new ArtemisPacketException(
					"Illegal packet length: " + len
			);
		}

		// connection type
		final int connectionTypeValue = readIntFromStream();
		final ConnectionType connectionType = ConnectionType.fromInt(connectionTypeValue);

		if (connectionType == null) {
			throw new ArtemisPacketException(
					"Unknown connection type: " + connectionTypeValue
			);
		}

		if (connectionType != connType) {
			throw new ArtemisPacketException(
					"Connection type mismatch: expected " + connType +
					", got " + connectionType
			);
		}

		// padding
		final int padding = readIntFromStream();

		if (padding != 0) {
			throw new ArtemisPacketException(
					"No empty padding after connection type?",
					connType
			);
		}

		// remaining bytes
		final int remainingBytes = readIntFromStream();
		final int expectedRemainingBytes = len - 20;

		if (remainingBytes != expectedRemainingBytes) {
			throw new ArtemisPacketException(
					"Packet length discrepancy: total length = " + len +
					"; expected " + expectedRemainingBytes +
					" for remaining bytes field, but got " +
					remainingBytes,
					connType
			);
		}

		// packet type
		final int packetType = readIntFromStream();

		// payload
		// The preamble was 24 bytes (6 ints), so the payload size is the size
		// of the whole packet minus 24 bytes.
		final int remaining = len - 24;
		byte[] payloadBytes = new byte[remaining];

		try {
			ByteArrayReader.readBytes(in, remaining, payloadBytes);
		} catch (InterruptedException ex) {
			throw new ArtemisPacketException(ex, connType, packetType);
		} catch (IOException ex) {
			throw new ArtemisPacketException(ex, connType, packetType);
		}

		debugger.onRecvPacketBytes(connType, packetType, payloadBytes);

		// Find the PacketFactory that knows how to handle this packet type
		PacketFactory factory = null;
		byte subtype = remaining > 0 ? payloadBytes[0] : 0x00;

		if (parse) {
			factory = factoryRegistry.get(connType, packetType, subtype);
		}

		ParseResult result = new ParseResult();
		Class<? extends ArtemisPacket> factoryClass;
		ArtemisPacket packet = null;

		if (factory != null) {
			// We've found a factory that can handle this packet; get the type
			// of packet it produces.
			factoryClass = factory.getFactoryClass();
		} else {
			// No factory can handle this; create an UnknownPacket
			UnknownPacket unkPkt = new UnknownPacket(connType, packetType, payloadBytes);
			debugger.onRecvUnparsedPacket(unkPkt);
			factoryClass = UnknownPacket.class;
			packet = unkPkt;
		}

		// Find out if any listeners are interested in this packet type
		result.setPacketListeners(listenerRegistry.listeningFor(factoryClass));

		// If it's an ObjectUpdatePacket, it might be transmitting an object
		// type that listeners are interested in, so check for that.
		if (factoryClass.isAssignableFrom(ObjectUpdatePacket.class)) {
			ObjectType type = ObjectType.fromId(subtype);

			if (type != null) {
				result.setObjectListeners(listenerRegistry.listeningFor(type.getObjectClass()));
			}
		}

		// IAN wants certain packet types even if the code consuming IAN isn't
		// interested in them.
		boolean required = REQUIRED_PACKET_TYPES.contains(Integer.valueOf(packetType));
		payload = new ByteArrayReader(payloadBytes);

		if (required || result.isInteresting()) {
			// We need this packet
			if (packet == null) {
				// It's not an UnknownPacket, so we need to parse it
				try {
					packet = factory.build(this);
				} catch (ArtemisPacketException ex) {
					throw new ArtemisPacketException(ex, connType, packetType, payloadBytes);
				} catch (RuntimeException ex) {
					throw new ArtemisPacketException(ex, connType, packetType, payloadBytes);
				}

				if (packet instanceof VersionPacket) {
					// We got a VersionPacket; store the version
					version = ((VersionPacket) packet).getVersion();
				}

				int unreadByteCount = payload.getBytesLeft();

				if (unreadByteCount > 0) {
					debugger.warn(
							"Unread bytes [" +
							packet.getClass().getSimpleName() + "]: " +
							TextUtil.byteArrayToHexString(readBytes(unreadByteCount))
					);
				}

				debugger.onRecvParsedPacket(packet);
			} else {
				payload.skip(payloadBytes.length);
			}
		} else {
			// Nothing is interested in this packet
			UnparsedPacket unpPkt = new UnparsedPacket(connType, packetType, payloadBytes);
			debugger.onRecvUnparsedPacket(unpPkt);
			packet = unpPkt;
			payload.skip(payloadBytes.length);
		}

		result.setPacket(packet);
		return result;
	}

	public int getBytesLeft() {
		return payload.getBytesLeft();
	}

	/**
	 * Returns true if the packet currently being read has more data; false
	 * otherwise.
	 */
	public boolean hasMore() {
		return payload.getBytesLeft() > 0 && (bitField == null || payload.peek() != 0);
	}

	/**
	 * Returns the next byte in the current packet's payload without moving the
	 * pointer.
	 */
	public byte peekByte() {
		return payload.peek();
	}

	/**
	 * Reads a single byte from the current packet's payload.
	 */
	public byte readByte() {
		return payload.readByte();
	}

	/**
	 * Convenience method for readByte(bit.ordinal(), defaultValue).
	 */
	public byte readByte(Enum<?> bit, byte defaultValue) {
		return readByte(bit.ordinal(), defaultValue);
	}

	/**
	 * Reads a single byte from the current packet's payload if the indicated
	 * bit in the current BitField is on. Otherwise, the pointer is not moved,
	 * and the given default value is returned.
	 */
	public byte readByte(int bitIndex, byte defaultValue) {
		return bitField.get(bitIndex) ? readByte() : defaultValue;
	}

	/**
	 * Reads the indicated number of bytes from the current packet's payload,
	 * then coerces the zeroeth byte read into a BoolState.
	 */
	public BoolState readBool(int byteCount) {
		return payload.readBoolState(byteCount);
	}

	/**
	 * Convenience method for readBool(bit.ordinal(), bytes).
	 */
	public BoolState readBool(Enum<?> bit, int bytes) {
		return readBool(bit.ordinal(), bytes);
	}

	/**
	 * Reads the indicated number of bytes from the current packet's payload if
	 * the indicated bit in the current BitField is on, then coerces the zeroeth
	 * byte read into a BoolState. Otherwise, the pointer is not moved, and
	 * BoolState.UNKNOWN is returned.
	 */
	public BoolState readBool(int bitIndex, int bytes) {
		return bitField.get(bitIndex) ? readBool(bytes) : BoolState.UNKNOWN;
	}

	/**
	 * Reads a short from the current packet's payload.
	 */
	public int readShort() {
		return payload.readShort();
	}

	/**
	 * Convenience method for readShort(bit.ordinal(), defaultValue).
	 */
	public int readShort(Enum<?> bit, int defaultValue) {
		return readShort(bit.ordinal(), defaultValue);
	}

	/**
	 * Reads a short from the current packet's payload if the indicated bit in
	 * the current BitField is on. Otherwise, the pointer is not moved, and the
	 * given default value is returned.
	 */
	public int readShort(int bitIndex, int defaultValue) {
		return bitField.get(bitIndex) ? readShort() : defaultValue;
	}

	/**
	 * Reads an int from the current packet's payload.
	 */
	public int readInt() {
		return payload.readInt();
	}

	/**
	 * Convenience method for readInt(bit.ordinal(), defaultValue).
	 */
	public int readInt(Enum<?> bit, int defaultValue) {
		return readInt(bit.ordinal(), defaultValue);
	}

	/**
	 * Reads an int from the current packet's payload if the indicated bit in
	 * the current BitField is on. Otherwise, the pointer is not moved, and the
	 * given default value is returned.
	 */
	public int readInt(int bitIndex, int defaultValue) {
		return bitField.get(bitIndex) ? readInt() : defaultValue;
	}

	/**
	 * Reads a float from the current packet's payload.
	 */
	public float readFloat() {
		return payload.readFloat();
	}

	/**
	 * Convenience method for readFloat(bit.ordinal(), defaultValue).
	 */
	public float readFloat(Enum<?> bit, float defaultValue) {
		return readFloat(bit.ordinal(), defaultValue);
	}

	/**
	 * Reads a float from the current packet's payload if the indicated bit in
	 * the current BitField is on. Otherwise, the pointer is not moved, and the
	 * given default value is returned.
	 */
	public float readFloat(int bitIndex, float defaultValue) {
		return bitField.get(bitIndex) ? readFloat() : defaultValue;
	}

	/**
	 * Reads a UTF-16LE String from the current packet's payload.
	 */
	public CharSequence readString() {
		return payload.readUtf16LeString();
	}

	/**
	 * Reads a US ASCII String from the current packet's payload.
	 */
	public String readUsAsciiString() {
		return payload.readUsAsciiString();
	}

	/**
	 * Convenience method for readString(bit.ordinal()).
	 */
	public CharSequence readString(Enum<?> bit) {
		return readString(bit.ordinal());
	}

	/**
	 * Reads a UTF-16LE String from the current packet's payload if the
	 * indicated bit in the current BitField is on. Otherwise, the pointer is
	 * not moved, and null is returned.
	 */
	public CharSequence readString(int bitIndex) {
		return bitField.get(bitIndex) ? readString() : null;
	}

	/**
	 * Reads the given number of bytes from the current packet's payload.
	 */
	public byte[] readBytes(int byteCount) {
		return payload.readBytes(byteCount);
	}

	/**
	 * Convenience method for readBytes(bit.ordinal(), byteCount).
	 */
	public byte[] readBytes(Enum<?> bit, int byteCount) {
		return readBytes(bit.ordinal(), byteCount);
	}

	/**
	 * Reads the given number of bytes from the current packet's payload if
	 * the indicated bit in the current BitField is on. Otherwise, the pointer
	 * is not moved, and null is returned.
	 */
	public byte[] readBytes(int bitIndex, int byteCount) {
		return bitField.get(bitIndex) ? readBytes(byteCount) : null;
	}

	/**
	 * Reads the given number of bytes from the current packet's payload and
	 * puts them in the unknown property map with the indicated name.
	 */
	public void readUnknown(String name, int byteCount) {
		unknownProps.put(name, readBytes(byteCount));
	}

	/**
	 * Reads the given number of bytes from the current packet's payload and
	 * puts them in the unknown object property map with the indicated name.
	 */
	public void readObjectUnknown(String name, int byteCount) {
		unknownObjectProps.put(name, readBytes(byteCount));
	}

	/**
	 * If the indicated bit in the current BitField is off, this method returns
	 * without doing anything. Otherwise, it acts as a convenience method for
	 * readObjectUnknown(bit.name(), byteCount).
	 */
	public void readObjectUnknown(Enum<?> bit, int byteCount) {
		if (bitField.get(bit.ordinal())) {
			readObjectUnknown(bit.name(), byteCount);
		}
	}

	/**
	 * If the indicated bit in the current BitField is off, this method returns
	 * without doing anything. Otherwise, it acts as a convenience method for
	 * readObjectUnknown(bit.name(), byteCount).
	 */
	public void readObjectUnknown(int bitIndex, int byteCount) {
		if (bitField.get(bitIndex)) {
			readObjectUnknown(BitField.generateBitName(bitIndex), byteCount);
		}
	}

	/**
	 * Skips the given number of bytes in the current packet's payload.
	 */
	public void skip(int byteCount) {
		payload.skip(byteCount);
	}

	/**
	 * Returns the unknown properties previously stored by readUnknown().
	 */
	public SortedMap<String, byte[]> getUnknownProps() {
		return unknownProps;
	}

	/**
	 * Starts reading an object from an ObjectUpdatingPacket. This will read off
	 * an object ID (int) and (if bitCount is greater than 0) a BitField from
	 * the current packet's payload. This also clears the unknownObjectProps
	 * property. The ObjectType is then returned.
	 */
	public ObjectType startObject(ObjectType type, int bitCount) {
		objectType = type;
		objectId = readInt();

		if (bitCount != 0) {
			bitField = payload.readBitField(bitCount);
		} else {
			bitField = null;
		}

		unknownObjectProps = new TreeMap<String, byte[]>();
		return objectType;
	}

	/**
	 * Convenience method for has(bit.ordinal).
	 */
	public boolean has(Enum<?> bit) {
		return has(bit.ordinal());
	}

	/**
	 * Returns true if the current BitField has the indicated bit turned on.
	 */
	public boolean has(int bitIndex) {
		return bitField.get(bitIndex);
	}

	/**
	 * Returns the type of the current object being read from the payload.
	 */
	public ObjectType getObjectType() {
		return objectType;
	}

	/**
	 * Returns the ID of the current object being read from the payload.
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * Returns the unknown object properties previously stored by
	 * readObjectUnknown().
	 */
	public SortedMap<String, byte[]> getUnknownObjectProps() {
		return unknownObjectProps;
	}

	/**
	 * Reads an int value directly from the InputStream wrapped by this object.
	 * This is used to read values for the preamble. This method blocks until
	 * four bytes are read or the stream closes. In the latter case, 
	 * ArtemisPacketException will be thrown.
	 */
	private int readIntFromStream() throws ArtemisPacketException {
		try {
			ByteArrayReader.readBytes(in, 4, intBuffer);
			return ByteArrayReader.readInt(intBuffer, 0);
		} catch (InterruptedException ex) {
			throw new ArtemisPacketException(ex);
		} catch (IOException ex) {
			throw new ArtemisPacketException(ex);
		}
	}
}