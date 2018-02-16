package com.walkertribe.ian.util;

/**
 * An implementation of the JamCRC algorithm. These checksums are used to allow
 * Artemis to send an int instead of a full string.
 * @author rjwut
 */
public final class JamCrc {
    private static final int INITIAL_VALUE = 0xffffffff;
    private static final int POLYNOMIAL = 0x04c11db7;
    private static final int[] TABLE = new int[256];

    static {
    	// pre-populate table
        for (int i = 0; i <= 0xff; i++) {
            TABLE[i] = reflect(i, (byte) 8) << 24;

            for (int j = 0; j < 8; j++) {
                TABLE[i] = (TABLE[i] << 1) ^ (((TABLE[i] & (1 << 31)) == 0) ? 0 : POLYNOMIAL);
            }

            TABLE[i] = reflect(TABLE[i], (byte) 32);    
        }
    }

    /**
     * Computes a checksum for the given byte array.
     */
    public static int compute(byte[] bytes) {
    	int crc = INITIAL_VALUE;

    	for (byte b : bytes) {
            crc = (crc >>> 8) ^ TABLE[(crc & 0xff) ^ (b & 0xff)];
        }

    	return crc;
    }

    /**
     * Computes a checksum for the UTF-8 representation of the given string.
     * Note that even though strings are transmitted in UTF-16LE in the Artemis
     * protocol, checksums are computed in UTF-8. Weirdness.
     */
    public static int compute(CharSequence str) {
    	return compute(str.toString().getBytes(Util.UTF8));
    }

    private static int reflect(int ref, byte ch) {
        int value = 0;

        for (int i = 1; i < (ch + 1); i++) {
            if ((ref & 1) != 0) {
                value |= 1 << (ch - i);
            }

            ref >>>= 1;
        }

        return value;
    }

    private JamCrc() {
    	// prevent instantiation
    }
}
