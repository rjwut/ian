package com.walkertribe.ian.util;

/**
 * Various utility methods for coercing between hex values and other formats.
 */
public class TextUtil {
	/**
	 * Converts the given byte to a hex String.
	 */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b);

        if (hex.length() >= 2) {
            return hex.substring(hex.length() - 2);
        }

        return String.format("0%s", hex);
    }

    /**
     * Converts the given byte array to a hex String.
     */
    public static String byteArrayToHexString(byte[] data) {
        return byteArrayToHexString(data, 0, data.length);
    }

    /**
     * Converts a subset of the given byte array to a hex String.
     */
    public static String byteArrayToHexString(byte[] data, int offset, int length) {
        StringBuilder buf = new StringBuilder();
        final int end = offset + length;

        for (int i = offset; i < end; i++) {
            buf.append(byteToHex(data[i]));
        }

        return buf.toString();
    }

    /**
     * Converts an int to a hex String.
     */
    public static String intToHex(int val) {
        String hex = Integer.toHexString(val);

        if (hex.length() >= 8) {
            return hex.substring(hex.length() - 8);
        }

        return String.format("%8s", hex).replace(' ', '0');
    }

    /**
     * Converts an int to a little-endian hex String.
     */
    public static String intToHexLE(int val) {
    	StringBuilder buf = new StringBuilder();

    	for (int i = 0; i < 32; i += 8) {
    		String hex = Integer.toHexString((0xff & (val >> i)));

    		if (hex.length() == 1) {
    			buf.append('0');
    		}

    		buf.append(hex);
    	}

    	return buf.toString();
    }

    /**
     * Converts a hex String to a byte array.
     */
    public static byte[] hexToByteArray(String hex) {
    	int len = hex.length();

    	if (len % 2 != 0) {
    		throw new IllegalArgumentException(
    				"Hex strings must contain two characters per byte: " + hex
    		);
    	}

    	byte[] bytes = new byte[len / 2];

    	for (int i = 0; i < len; i += 2) {
    		String hexByte = hex.substring(i, i + 2);
    		bytes[i / 2] = (byte) Integer.parseInt(hexByte, 16);
    	}

    	return bytes;
    }
}