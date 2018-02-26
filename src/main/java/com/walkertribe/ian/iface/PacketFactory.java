package com.walkertribe.ian.iface;

import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Interface for objects which can convert a byte array to a packet.
 * @author rjwut
 */
public interface PacketFactory<T extends ArtemisPacket> {
	/**
	 * Returns the class of ArtemisPacket that this PacketFactory can produce.
	 * Note: It is legal to have more than one factory producing the same
	 * Class.
	 */
	public Class<T> getFactoryClass();

	/**
	 * Returns a packet constructed with a payload read from the given
	 * PacketReader. (It is assumed that the preamble has already been read.)
	 * This method should throw an ArtemisPacketException if the payload is
	 * malformed.
	 */
	public T build(PacketReader reader) throws ArtemisPacketException;
}