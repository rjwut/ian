package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Interface for classes which can parse objects from the payload of an
 * ObjectUpdatePacket.
 * @author rjwut
 */
public interface ObjectParser {
	/**
	 * Returns the number of bits in the bit field representing the available
	 * properties for this type of object. If this value is greater than zero,
	 * the object will start with a BitField which describes which of these
	 * properties are present.
	 */
	public int getBitCount();

	/**
	 * Reads and returns an object from the payload. If there are no more
	 * objects in the payload, parse() returns null.
	 */
	public ArtemisObject parse(PacketReader reader);

	/**
	 * Writes the given object to the payload.
	 */
	public void write(ArtemisObject obj, PacketWriter writer);
}
