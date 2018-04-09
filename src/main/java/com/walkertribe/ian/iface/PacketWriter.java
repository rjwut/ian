package com.walkertribe.ian.iface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.SortedMap;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.util.BitField;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.NullTerminatedString;
import com.walkertribe.ian.util.Util;
import com.walkertribe.ian.util.Version;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * <p>
 * Facilitates writing packets to an OutputStream. This object may be reused to
 * write as many packets as desired to a single OutputStream. To write a packet,
 * follow these steps:
 * </p>
 * <ol>
 * <li>Invoke start().</li>
 * <li>Write the payload data using the write*() methods. Payload data is
 * buffered by the PacketWriter, not written immediately to the
 * OutputStream.</li>
 * <li>Invoke flush(). The proper values for the fields in the preamble will be
 * automatically computed and written, followed by the payload. The entire
 * packet is then flushed to the OutputStream.</li>
 * </ol>
 * <p>
 * Once flush() has been called, you can start writing another packet by
 * invoking start() again.
 * </p>
 * @author rjwut
 */
public class PacketWriter {
	private final OutputStream out;
	private Origin mOrigin;
	private Version version;

	private int mPacketType;
	private ByteArrayOutputStream baos;
	private ArtemisObject obj;
	private ObjectType objType;
	private BitField bitField;
	private ByteArrayOutputStream baosObj;
	private byte[] buffer = new byte[4];

	/**
	 * Creates a PacketWriter that writes packets to the given OutputStream.
	 */
	public PacketWriter(OutputStream out) {
		if (out == null) {
			throw new IllegalArgumentException(
					"The out argument cannot be null"
			);
		}

		this.out = out;
	}

	/**
	 * Returns the server version number, or null if unknown.
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * Sets the server version number. This is invoked by VersionPacket. 
	 */
	public void setVersion(Version version) {
		this.version = version;
	}

	/**
	 * Starts a packet of the given type.
	 */
	public PacketWriter start(Origin origin, int packetType) {
		mOrigin = origin;
		mPacketType = packetType;
		baos = new ByteArrayOutputStream();
		return this;
	}

	/**
	 * Convenience method for startObject(object, bits.length).
	 */
	public PacketWriter startObject(ArtemisObject object, ObjectType type, Enum<?>[] bits) {
		return startObject(object, type, bits.length);
	}

	/**
	 * Starts writing a new entry into the packet for the given object,
	 * overriding the object's type with the specified ObjectType.
	 */
	public PacketWriter startObject(ArtemisObject object, ObjectType type, int bitFieldSize) {
		assertStarted();
		obj = object;
		objType = type;
		bitField = new BitField(bitFieldSize);
		baosObj = new ByteArrayOutputStream();
		return this;
	}

	/**
	 * Writes a single byte. You must invoke start() before calling this method.
	 */
	public PacketWriter writeByte(byte v) {
		assertStarted();
		baos.write(v);
		return this;
	}

	/**
	 * Convenience method for writeByte(bit.ordinal(), v, defaultValue).
	 */
	public PacketWriter writeByte(Enum<?> bit, byte v, byte defaultValue) {
		return writeByte(bit.ordinal(), v, defaultValue);
	}

	/**
	 * If the given byte is different from defaultValue, the byte is written
	 * to the packet, and the corresponding bit in the object's bit field is set;
	 * otherwise, nothing happens. You must invoke startObject() before calling
	 * this method.
	 */
	public PacketWriter writeByte(int bitIndex, byte v, byte defaultValue) {
		assertObjectStarted();

		if (v != defaultValue) {
			bitField.set(bitIndex, true);
			baosObj.write(v);
		}

		return this;
	}

	/**
	 * Writes a BoolState.
	 */
	public PacketWriter writeBool(BoolState v, int byteCount) {
		writeBytes(v.toByteArray(byteCount));
		return this;
	}

	/**
	 * Convenience method for writeBool(bit.ordinal(), v, byteCount).
	 */
	public PacketWriter writeBool(Enum<?> bit, BoolState v, int byteCount) {
		return writeBool(bit.ordinal(), v, byteCount);
	}

	/**
	 * If the given BoolState is known, it is written to the packet using the
	 * given number of bytes, and the corresponding bit in the object's bit
	 * field is set; otherwise, nothing happens. You must invoke startObject()
	 * before calling this method.
	 */
	public PacketWriter writeBool(int bitIndex, BoolState v, int byteCount) {
		writeBytes(bitIndex, v.toByteArray(byteCount));
		return this;
	}

	/**
	 * Writes a short (two bytes). You must invoke start() before calling this
	 * method.
	 */
	public PacketWriter writeShort(int v) {
		assertStarted();
		writeShort(v, baos);
		return this;
	}

	/**
	 * Convenience method for writeShort(bit.ordinal(), v, defaultValue).
	 */
	public PacketWriter writeShort(Enum<?> bit, int v, int defaultValue) {
		return writeShort(bit.ordinal(), v, defaultValue);
	}

	/**
	 * If the given int is different from defaultValue, the int is coerced to a
	 * short and written to the packet, and the corresponding bit in the
	 * object's bit field is set; otherwise, nothing happens. You must invoke
	 * startObject() before calling this method.
	 */
	public PacketWriter writeShort(int bitIndex, int v, int defaultValue) {
		assertObjectStarted();

		if (v != defaultValue) {
			bitField.set(bitIndex, true);
			writeShort(v, baosObj);
		}

		return this;
	}

	/**
	 * Writes an int (four bytes). You must invoke start() before calling this
	 * method.
	 */
	public PacketWriter writeInt(int v) {
		assertStarted();
		writeInt(v, baos);
		return this;
	}

	/**
	 * Convenience method for writeInt(bit.ordinal(), v, defaultValue).
	 */
	public PacketWriter writeInt(Enum<?> bit, int v, int defaultValue) {
		return writeInt(bit.ordinal(), v, defaultValue);
	}

	/**
	 * If the given int is different from defaultValue, the int is written to
	 * the packet, and the corresponding bit in the object's bit field is set;
	 * otherwise, nothing happens. You must invoke startObject() before calling
	 * this method.
	 */
	public PacketWriter writeInt(int bitIndex, int v, int defaultValue) {
		assertObjectStarted();

		if (v != defaultValue) {
			bitField.set(bitIndex, true);
			writeInt(v, baosObj);
		}

		return this;
	}

	/**
	 * Writes a float (four bytes). You must invoke start() before calling this
	 * method.
	 */
	public PacketWriter writeFloat(float v) {
		return writeInt(Float.floatToRawIntBits(v));
	}

	/**
	 * Convenience method for writeFloat(bit.ordinal(), v).
	 */
	public PacketWriter writeFloat(Enum<?> bit, float v) {
		return writeFloat(bit.ordinal(), v);
	}

	/**
	 * If the given float is not a NaN, the float is written to the packet, and
	 * the corresponding bit in the object's bit field is set; otherwise,
	 * nothing happens. You must invoke startObject() before calling this
	 * method.
	 */
	public PacketWriter writeFloat(int bitIndex, float v) {
		assertObjectStarted();

		if (!Float.isNaN(v)) {
			bitField.set(bitIndex, true);
			writeInt(Float.floatToRawIntBits(v), baosObj);
		}

		return this;
	}

	/**
	 * Unconditionally write a float to the packet for the current object. This
	 * is needed in cases where the server can possibly send NaN for a property
	 * value.
	 */
	public PacketWriter writeObjectFloat(Enum<?> bit, float v) {
		return writeObjectFloat(bit.ordinal(), v);
	}

	/**
	 * Unconditionally write a float to the packet for the current object. This
	 * is needed in cases where the server can possibly send NaN for a property
	 * value.
	 */
	public PacketWriter writeObjectFloat(int bitIndex, float v) {
		assertObjectStarted();
		bitField.set(bitIndex, true);
		writeInt(Float.floatToRawIntBits(v), baosObj);
		return this;
	}

	/**
	 * Writes a UTF-16LE encoded CharSequence. This handles writing the
	 * string length and the terminating null character automatically. You must
	 * invoke start() before calling this method.
	 */
	public PacketWriter writeString(CharSequence str) {
		writeString(str, baos);
		return this;
	}

	/**
	 * Writes a US-ASCII encoded String. This handles writing the string length
	 * automatically. You must invoke start() before calling this method.
	 */
	public PacketWriter writeUsAsciiString(String str) {
		writeUsAsciiString(str, baos);
		return this;
	}

	/**
	 * Convenience method for writeString(bit.ordinal(), str).
	 */
	public PacketWriter writeString(Enum<?> bit, CharSequence str) {
		return writeString(bit.ordinal(), str);
	}

	/**
	 * If the given CharSequence is not null, it is written to the packet, and
	 * the corresponding bit in the object's bit field is set; otherwise,
	 * nothing happens. You must invoke startObject() before calling this
	 * method.
	 */
	public PacketWriter writeString(int bitIndex, CharSequence str) {
		assertObjectStarted();

		if (str != null) {
			bitField.set(bitIndex, true);
			writeString(str, baosObj);
		}

		return this;
	}

	/**
	 * Writes a byte array. You must invoke start() before calling this method.
	 */
	public PacketWriter writeBytes(byte[] bytes) {
		assertStarted();
		baos.write(bytes, 0, bytes.length);
		return this;
	}

	/**
	 * Convenience method for writeBytes(bit.ordinal(), bytes).
	 */
	public PacketWriter writeBytes(Enum<?> bit, byte[] bytes) {
		return writeBytes(bit.ordinal(), bytes);
	}

	/**
	 * If the given byte array is not null, it is written to the packet, and the
	 * corresponding bit in the object's bit field is set; otherwise, nothing
	 * happens. You must invoke startObject() before calling this method.
	 */
	public PacketWriter writeBytes(int bitIndex, byte[] bytes) {
		assertObjectStarted();

		if (bytes != null) {
			bitField.set(bitIndex, true);
			baosObj.write(bytes, 0, bytes.length);
		}

		return this;
	}

	/**
	 * Retrieves the named unknown value as a byte array from the unknown
	 * properties map. If the retrieved value is not null, it is written to the
	 * packet; otherwise, the defaultValue byte array will be written. You must
	 * invoke startObject() before calling this method.
	 */
	public PacketWriter writeUnknown(String name, byte[] defaultValue) {
		assertObjectStarted();
		SortedMap<String, byte[]> unknownProps = obj.getUnknownProps();
		byte[] bytes = unknownProps != null ? unknownProps.get(name) : null;
		bytes = bytes != null ? bytes : defaultValue;
		baosObj.write(bytes, 0, bytes.length);
		return this;
	}

	/**
	 * Retrieves the unknown value identified by the indicated bit as a byte
	 * array from the unknown properties map. If the retrieved value is not
	 * null, it is written to the packet and the corresponding bit in the
	 * object's bit field is set; otherwise, nothing happens. You must invoke
	 * startObject() before calling this method.
	 */
	public PacketWriter writeUnknown(Enum<?> bit) {
		return writeUnknown(bit.ordinal(), bit.name());
	}

	/**
	 * Retrieves the unknown value identified by the indicated bit as a byte
	 * array from the unknown properties map. If the retrieved value is not
	 * null, it is written to the packet and the corresponding bit in the
	 * object's bit field is set; otherwise, nothing happens. You must invoke
	 * startObject() before calling this method.
	 */
	public PacketWriter writeUnknown(int bitIndex) {
		return writeUnknown(bitIndex, BitField.generateBitName(bitIndex));
	}

	/**
	 * Retrieves the unknown value identified by the indicated bit as a byte
	 * array from the unknown properties map. If the retrieved value is not
	 * null, it is written to the packet and the corresponding bit in the
	 * object's bit field is set; otherwise, nothing happens. You must invoke
	 * startObject() before calling this method.
	 */
	private PacketWriter writeUnknown(int bitIndex, String bitName) {
		assertObjectStarted();
		SortedMap<String, byte[]> unknownProps = obj.getUnknownProps();

		if (unknownProps != null) {
			byte[] bytes = unknownProps.get(bitName);
	
			if (bytes != null) {
				bitField.set(bitIndex, true);
				baosObj.write(bytes, 0, bytes.length);
			}
		}

		return this;
	}

	/**
	 * Flushes the current object's bytes to the packet, but not to the wrapped
	 * OutputStream. This prepares the packet for writing another byte. You must
	 * invoke startObject() before calling this method. When this method
	 * returns, you will have to call startObject() again before you can write
	 * another object.
	 */
	public void endObject() {
		writeByte(objType.getId());
		writeInt(obj.getId());

		try {
			bitField.write(baos);
		} catch (IOException ex) {
			// ByteArrayOutputStream doesn't actually throw IOException; it
			// just inherits the declaration from the OutputStream class. Since
			// it won't ever actually happen, we wrap it in a RuntimeException.
			throw new RuntimeException(ex);
		}

		writeBytes(baosObj.toByteArray());
		obj = null;
		objType = null;
		bitField = null;
		baosObj = null;
	}

	/**
	 * Writes the completed packet to the OutputStream. You must invoke start()
	 * before calling this method. When this method returns, you will have to
	 * call start() again before you can write more data. The given Debugger
	 * will also be notified.
	 */
	public void flush(Debugger debugger) throws IOException {
		assertStarted();
		byte[] payload = baos.toByteArray();
		baos = null;
		writeInt(ArtemisPacket.HEADER, out); // header
		writeInt(payload.length + 24, out);  // packet length
		writeInt(mOrigin.toInt(), out);      // connection type
		writeInt(0, out);                    // padding
		writeInt(payload.length + 4, out);   // remaining bytes
		writeInt(mPacketType, out);          // packet type
		out.write(payload);                  // payload
		out.flush();
		debugger.onSendPacketBytes(mOrigin, mPacketType, payload);
	}

	/**
	 * Throws an IllegalStateException if start() has not been called since the
	 * time this object was constructed or since the last call to flush().
	 */
	private void assertStarted() {
		if (baos == null) {
			throw new IllegalStateException("Must invoke start() first");
		}
	}

	/**
	 * Throws an IllegalStateException if startObject() has not been called
	 * since the time this object was constructed or since the last call to
	 * endObject() or flush().
	 */
	private void assertObjectStarted() {
		if (baosObj == null) {
			throw new IllegalStateException("Must invoke startObject() first");
		}
	}

	/**
	 * Writes an int (coerced into a short) into the given ByteArrayOutputStream.
	 */
	private void writeShort(int value, ByteArrayOutputStream outStream) {
		buffer[0] = (byte) (0xff & value);
		buffer[1] = (byte) (0xff & (value >> 8));
		outStream.write(buffer, 0, 2);
	}

	/**
	 * Writes an int into the given OutputStream.
	 */
	private void writeInt(int v, OutputStream outStream) throws IOException {
		buffer[0] = (byte) (0xff & v);
		buffer[1] = (byte) (0xff & (v >> 8));
		buffer[2] = (byte) (0xff & (v >> 16));
		buffer[3] = (byte) (0xff & (v >> 24));
		outStream.write(buffer, 0, 4);
	}

	/**
	 * Writes an int into the given ByteArrayOutputStream. This is a useful override because it can
	 * avoid throwing IOException.
	 */
	private void writeInt(int v, ByteArrayOutputStream outStream) {
		buffer[0] = (byte) (0xff & v);
		buffer[1] = (byte) (0xff & (v >> 8));
		buffer[2] = (byte) (0xff & (v >> 16));
		buffer[3] = (byte) (0xff & (v >> 24));
		outStream.write(buffer, 0, 4);
	}

	/**
	 * Writes a UTF-16LE encoded CharSequence into the given
	 * ByteArrayOutputStream.
	 */
	private void writeString(CharSequence v, ByteArrayOutputStream outStream) {
		int charCount;

		if (v instanceof NullTerminatedString) {
			charCount = ((NullTerminatedString) v).fullLength();
		} else {
			charCount = v.length() + 1;
		}

		writeInt(charCount, outStream);
		byte[] charBytes = v.toString().getBytes(Util.UTF16LE);
		outStream.write(charBytes, 0, charBytes.length);
		writeShort(0, outStream); // terminating null

		if (v instanceof NullTerminatedString) {
			byte[] garbage = ((NullTerminatedString) v).getGarbage();

			if (garbage.length != 0) {
				outStream.write(garbage, 0, garbage.length);
			}
		}
	}

	/**
	 * Writes a US ASCII encoded String into the given ByteArrayOutputStream.
	 */
	private void writeUsAsciiString(String v, ByteArrayOutputStream outStream) {
		int charCount = v.length();
		writeInt(charCount, outStream);
		byte[] charBytes = v.getBytes(Util.US_ASCII);
		outStream.write(charBytes, 0, charBytes.length);
	}
}