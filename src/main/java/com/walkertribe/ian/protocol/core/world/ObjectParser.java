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
	 * Returns an array of enum values representing the available properties
	 * for this type of object. The object will start with a BitField which
	 * describes which of these properties are present.
	 */
	public Enum<?>[] getBits();

	/**
	 * Reads and returns an object from the payload. If there are no more
	 * objects in the payload, parse() returns null.
	 */
	public ArtemisObject parse(PacketReader reader);

	/**
	 * Writes the given object to the payload.
	 */
	public void write(ArtemisObject obj, PacketWriter writer);

	/**
	 * Writes the details of the given object to the indicated StringBuilder.
	 */
	public void appendDetail(ArtemisObject obj, StringBuilder b);
}
