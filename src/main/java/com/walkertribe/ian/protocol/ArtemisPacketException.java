package com.walkertribe.ian.protocol;

import java.io.PrintStream;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.util.TextUtil;

/**
 * Thrown when IAN encounters a problem while attempting to read or write a
 * packet of a known type. Unknown packets don't throw this exception; IAN
 * creates UnknownPacket objects for them.
 */
public class ArtemisPacketException extends Exception {
    private static final long serialVersionUID = 6305993950844264082L;
    private Origin origin;
    private int packetType;
    private byte[] payload;
    private boolean fatal;

    /**
     * @param string A description of the problem
     */
    public ArtemisPacketException(String string) {
        super(string);
        fatal = true;
    }

    /**
     * @param t The exception that caused ArtemisPacketException to be thrown
     */
    public ArtemisPacketException(Throwable t, boolean fatal) {
        super(t);
        this.fatal = fatal;
    }

    /**
     * @param t The exception that caused ArtemisPacketException to be thrown
     * @param origin The packet's Origin
     * @param packetType The packet's type value
     * @param payload The packet's payload bytes
     */
    public ArtemisPacketException(Throwable t, Origin origin, int packetType, byte[] payload,
            boolean fatal) {
        super(t);
        this.fatal = true;
        appendParsingDetails(origin, packetType, payload);
    }

    /**
     * @param string A description of the problem
     * @param origin The packet's Origin
     * @param packetType The packet's type value
     * @param payload The packet's payload bytes
     */
    public ArtemisPacketException(String string, Origin origin, int packetType, byte[] payload,
            boolean fatal) {
        super(string);
        this.fatal = true;
        appendParsingDetails(origin, packetType, payload);
    }

    /**
     * Returns the packet's Origin, or null if unknown.
     */
    public Origin getOrigin() {
    	return origin;
    }

    /**
     * Returns the type value for this packet, or 0 if unknown.
     */
    public int getPacketType() {
    	return packetType;
    }

    /**
     * Returns the payload for this packet, or null if unknown.
     */
    public byte[] getPayload() {
    	return payload;
    }

    /**
     * Returns true if the exception is fatal.
     */
    public boolean isFatal() {
        return fatal;
    }

    /**
     * Adds the Origin, packet type and payload to this exception.
     */
    public void appendParsingDetails(Origin origin, int packetType, byte[] payload) {
        this.origin = origin;
        this.packetType = packetType;
        this.payload = payload;
    }

    /**
     * Dumps the packet bytes to System.err.
     */
    public void printPacketDump() {
    	printPacketDump(System.err);
    }

    /**
     * Dumps the packet bytes to the given PrintStream.
     */
    public void printPacketDump(PrintStream err) {
		err.println(origin + ": " + TextUtil.intToHex(packetType) + " " +
				(payload == null ? "" : TextUtil.byteArrayToHexString(payload))
		);
    }

    @Override
    public void printStackTrace(PrintStream s) {
        printPacketDump(s);
        super.printStackTrace(s);
    }

    /**
     * Convert the data in this exception to an UnknownPacket. An
     * IllegalStateException will occur if the Origin or payload is null.
     */
	public UnknownPacket toUnknownPacket() {
		if (origin == null) {
			throw new IllegalStateException("Unknown origin");
		}

		if (payload == null) {
			throw new IllegalStateException("Unknown payload");
		}

		return new UnknownPacket(origin, packetType, payload);
	}
}