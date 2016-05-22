package com.walkertribe.ian.protocol;

/**
 * Thrown if IAN encounters a different packet type than it expected. This is
 * almost certainly caused by a bug in the PacketFactory that constructed the
 * packet.
 * @author rjwut
 */
public class UnexpectedTypeException extends IllegalArgumentException {
	private static final long serialVersionUID = -5961855010011595291L;

	public UnexpectedTypeException(int type, int expectedType) {
		super("Expected type " + expectedType + ", got " + type);
	}
}
