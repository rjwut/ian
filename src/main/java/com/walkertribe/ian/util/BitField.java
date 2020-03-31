package com.walkertribe.ian.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * <p>
 * Provides easy reading and writing of bits in a bit field. The bytes are
 * little-endian, so in the event that the final byte is not completely
 * utilized, it will be the most significant bits that are left unused. The
 * bits in a bit field are commonly represented as enums in IAN, but BitField
 * does not require this.
 * </p>
 * <p>
 * Note that Artemis will never fully utilize the entire bit field. If the
 * number of bits actually used is divisible by eight, Artemis will add another
 * (unused) byte to the end of the field. So a field with eight bits will be
 * two bytes wide instead of one like you would expect, and the second byte
 * will always be <code>0x00</code>.
 * </p>
 * @author rjwut
 */
public class BitField {
	private byte[] bytes;

	/**
	 * Creates a BitField large enough to accommodate the given number of bits.
	 * All bits start at 0.
	 */
	public BitField(int bitCount) {
		this.bytes = new byte[countBytes(bitCount)];
	}

	/**
	 * Creates a BitField large enough to accommodate the enumerated bits, and
	 * stores the indicated bytes in it.
	 */
	public BitField(int bitCount, byte[] bytes, int offset) {
		this.bytes = Arrays.copyOfRange(bytes, offset, offset + countBytes(bitCount));
	}

	/**
	 * Returns the number of bytes in this BitField.
	 */
	public int getByteCount() {
		return bytes.length;
	}

	/**
	 * Returns true if the indicated bit is 1, false if it's 0.
	 */
	public boolean get(int bitIndex) {
		int byteIndex =  bitIndex / 8;
		int mask = 0x1 << (bitIndex % 8);
		return (bytes[byteIndex] & mask) != 0;
	}

	/**
	 * If value is true, the indicated bit is set to 1; otherwise, it's set to
	 * 0.
	 */
	public void set(int bitIndex, boolean value) {
		int byteIndex = bitIndex / 8;
		bitIndex = bitIndex % 8;
		int mask = (0x1 << bitIndex) ^ 0xff;
		int shiftedValue = (value ? 1 : 0) << bitIndex;
		bytes[byteIndex] = (byte) ((bytes[byteIndex] & mask) | shiftedValue);
	}

	/**
	 * Returns a hex encoded String of the bytes that comprise this BitField.
	 */
	@Override
	public String toString() {
		return TextUtil.byteArrayToHexString(bytes);
	}

	/**
	 * Writes this BitField to the given OutputStream.
	 */
	public void write(OutputStream out) throws IOException {
		out.write(bytes);
	}

	/**
	 * Convenience method for listActiveBits(bits, ' ').
	 */
	public String listActiveBits(Enum<?>[] bits) {
		return listActiveBits(bits, ' ');
	}

	/**
	 * Returns a delimited list of the names of the enum values that correspond
	 * to active bits in this BitField. This can be useful for debugging
	 * purposes.
	 */
	public String listActiveBits(Enum<?>[] bits, char delimiter) {
		StringBuilder list = new StringBuilder();

		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];

			for (int j = 0; j < 8; j++) {
				int bitIndex = i * 8 + j;

				if (bitIndex < bits.length) {
					if ((b & (0x01 << j)) != 0) {
						if (list.length() != 0) {
							list.append(delimiter);
						}

						list.append(bits[bitIndex].name());
					}
				}
			}
		}

		return list.toString();
	}

	/**
	 * Returns the number of bytes required to store the given number of bits in a BitField. Note
	 * that Artemis allocates an extra, unused byte whenever there are no leftover bits in the last
	 * byte.
	 */
	public static int countBytes(int bitCount) {
		return (bitCount + 8) / 8;
	}

	/**
	 * Generates a name for an unknown bit in the form UNK_{byte}_{bit},
	 * where the byte and bit values are one-based.
	 */
	public static String generateBitName(int bitIndex) {
		return "UNK_" + ((bitIndex / 8) + 1) + "_" + ((bitIndex % 8) + 1);
	}
}