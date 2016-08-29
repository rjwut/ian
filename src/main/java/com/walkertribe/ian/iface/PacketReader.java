package com.walkertribe.ian.iface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedMap;
import java.util.TreeMap;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.UnknownPacket;
import com.walkertribe.ian.protocol.UnparsedPacket;
import com.walkertribe.ian.protocol.core.setup.VersionPacket;
import com.walkertribe.ian.protocol.core.setup.WelcomePacket;
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
	public PacketReader(ConnectionType connType, InputStream in,
			PacketFactoryRegistry factoryRegistry,
			ListenerRegistry listenerRegistry) {
		this.connType = connType;
		this.in = in;
		this.factoryRegistry = factoryRegistry;
		this.listenerRegistry = listenerRegistry;
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

		// IAN needs to parse the WelcomePacket and VersionPacket, even if the
		// client isn't interested in them.
		boolean required = packetType == WelcomePacket.TYPE || packetType == VersionPacket.TYPE;

		if (required || result.isInteresting()) {
			// We need this packet
			payload = new ByteArrayReader(payloadBytes);

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
			}

			debugger.onRecvParsedPacket(packet);
		} else {
			// Nothing is interested in this packet
			UnparsedPacket unpPkt = new UnparsedPacket(connType, packetType, payloadBytes);
			debugger.onRecvUnparsedPacket(unpPkt);
			packet = unpPkt;
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
	 * Convenience method for readByte(bit, 0).
	 */
	public byte readByte(Enum<?> bit) {
		return readByte(bit, (byte) 0);
	}

	/**
	 * Reads a single byte from the current packet's payload if the indicated
	 * bit in the current BitField is on. Otherwise, the pointer is not moved,
	 * and the given default value is returned.
	 */
	public byte readByte(Enum<?> bit, byte defaultValue) {
		return bitField.get(bit) ? readByte() : defaultValue;
	}

	/**
	 * Reads the indicated number of bytes from the current packet's payload,
	 * then coerces the zeroeth byte read into a BoolState.
	 */
	public BoolState readBool(int byteCount) {
		return payload.readBoolState(byteCount);
	}

	/**
	 * Reads the indicated number of bytes from the current packet's payload if
	 * the indicated bit in the current BitField is on, then coerces the zeroeth
	 * byte read into a BoolState. Otherwise, the pointer is not moved, and
	 * BoolState.UNKNOWN is returned.
	 */
	public BoolState readBool(Enum<?> bit, int bytes) {
		return bitField.get(bit) ? readBool(bytes) : BoolState.UNKNOWN;
	}

	/**
	 * Reads a short from the current packet's payload.
	 */
	public int readShort() {
		return payload.readShort();
	}

	/**
	 * Convenience method for readShort(bit, 0).
	 */
	public int readShort(Enum<?> bit) {
		return readShort(bit, 0);
	}

	/**
	 * Reads a short from the current packet's payload if the indicated bit in
	 * the current BitField is on. Otherwise, the pointer is not moved, and the
	 * given default value is returned.
	 */
	public int readShort(Enum<?> bit, int defaultValue) {
		return bitField.get(bit) ? readShort() : defaultValue;
	}

	/**
	 * Reads an int from the current packet's payload.
	 */
	public int readInt() {
		return payload.readInt();
	}

	/**
	 * Convenience method for readInt(bit, -1).
	 */
	public int readInt(Enum<?> bit) {
		return readInt(bit, -1);
	}

	/**
	 * Reads an int from the current packet's payload if the indicated bit in
	 * the current BitField is on. Otherwise, the pointer is not moved, and the
	 * given default value is returned.
	 */
	public int readInt(Enum<?> bit, int defaultValue) {
		return bitField.get(bit) ? readInt() : defaultValue;
	}

	/**
	 * Reads a float from the current packet's payload.
	 */
	public float readFloat() {
		return payload.readFloat();
	}

	/**
	 * Reads a float from the current packet's payload if the indicated bit in
	 * the current BitField is on. Otherwise, the pointer is not moved, and the
	 * given default value is returned.
	 */
	public float readFloat(Enum<?> bit, float defaultValue) {
		return bitField.get(bit) ? readFloat() : defaultValue;
	}

	/**
	 * Reads a UTF-16LE String from the current packet's payload.
	 */
	public String readString() {
		return payload.readUtf16LeString();
	}

	/**
	 * Reads a US ASCII String from the current packet's payload.
	 */
	public String readUsAsciiString() {
		return payload.readUsAsciiString();
	}

	/**
	 * Reads a UTF-16LE String from the current packet's payload if the
	 * indicated bit in the current BitField is on. Otherwise, the pointer is
	 * not moved, and null is returned.
	 */
	public String readString(Enum<?> bit) {
		return bitField.get(bit) ? readString() : null;
	}

	/**
	 * Reads the given number of bytes from the current packet's payload.
	 */
	public byte[] readBytes(int byteCount) {
		return payload.readBytes(byteCount);
	}

	/**
	 * Reads the given number of bytes from the current packet's payload if
	 * the indicated bit in the current BitField is on. Otherwise, the pointer
	 * is not moved, and null is returned.
	 */
	public byte[] readBytes(Enum<?> bit, int byteCount) {
		return bitField.get(bit) ? readBytes(byteCount) : null;
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
	 * Reads bytes from the current packet's payload until the endByte value is
	 * encountered, then puts them in the unknown object property map with the
	 * indicated name. This method is needed for the UpgradesParser, as we do
	 * not know the sizes of some of the fields. If we discover the sizes of the
	 * remaining fields, this method could probably go away.
	 */
	public void readObjectUnknownUntil(String name, byte endByte) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		while (hasMore() && peekByte() != endByte) {
			bytes.write(readByte());
		}

		unknownObjectProps.put(name, bytes.toByteArray());
	}

	/**
	 * If the indicated bit in the current BitField is off, this method returns
	 * without doing anything. Otherwise, it acts as a convenience method for
	 * readObjectUnknown(bit.name(), byteCount).
	 */
	public void readObjectUnknown(Enum<?> bit, int byteCount) {
		if (bitField.get(bit)) {
			readObjectUnknown(bit.name(), byteCount);
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
	 * an object ID (int) and (if a bits enum value array is given) a BitField
	 * from the current packet's payload. This also clears the
	 * unknownObjectProps property. The ObjectType is then returned.
	 */
	public ObjectType startObject(ObjectType type, Enum<?>[] bits) {
		objectType = type;
		objectId = readInt();

		if (bits != null) {
			bitField = payload.readBitField(bits);
		} else {
			bitField = null;
		}

		unknownObjectProps = new TreeMap<String, byte[]>();
		return objectType;
	}

	/**
	 * Returns true if the current BitField has the indicated bit turned on.
	 */
	public boolean has(Enum<?> bit) {
		return bitField.get(bit);
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