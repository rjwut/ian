package com.walkertribe.ian.protocol;

/**
 * Interface for objects which define packet types. This interface would most
 * properly be applied to an enum.
 * @author rjwut
 */
public interface PacketType {
	/**
	 * Returns a JamCRC hash of the packet type's internal name. This is used
	 * to identify the packet type in the protocol.
	 */
	public int getHash();

	/**
	 * Returns the internal name of the packet type. The hash is computed from
	 * this.
	 */
	public String getInternalName();
}