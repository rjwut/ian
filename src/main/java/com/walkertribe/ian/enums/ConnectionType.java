package com.walkertribe.ian.enums;

/**
 * Represents the type of the machine found at the opposite end of a connection.
 * @author rjwut
 */
public enum ConnectionType {
	SERVER, CLIENT;

	/**
	 * Returns the ConnectionType that corresponds to the given int value.
	 */
	public static final ConnectionType fromInt(int value) {
		return value == 1 ? SERVER : (value == 2 ? CLIENT : null);
	}

	private int val;

	ConnectionType() {
		val = ordinal() + 1;
	}

	/**
	 * Returns the int value for this ConnectionType.
	 */
	public int toInt() {
		return val;
	}

	/**
	 * Returns the opposite ConnectionType to this one: SERVER.opposite()
	 * returns CLIENT and vice-versa.
	 */
	public ConnectionType opposite() {
		return this == SERVER ? CLIENT : SERVER;
	}
}