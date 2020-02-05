package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * <p>
 * Sent by the server to indicate that an object is exploding.
 * </p>
 * <p>
 * There are two simpleEvent subtypes involved here: 0x00 (EXPLOSION) and 0x13 (DETONATION). 0x13
 * is transmitted when a mine or torpedo detonates (in other words, CAUSES damage by exploding),
 * whereas 0x00 is transmitted when an object explodes BECAUSE of damage it incurs. This class
 * covers both simpleEvent subtypes; you can invoke getObjectType() to determine which case is
 * applicable.
 * </p>
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = { SubType.EXPLOSION, SubType.DETONATION })
public class ExplosionPacket extends SimpleEventPacket {
	private ObjectType mObjectType;
	private int mObjectId;

	/**
	 * Indicates that the given ArtemisObject is exploding.
	 */
	public ExplosionPacket(ArtemisObject obj) {
		this(obj.getType(), obj.getId());
	}

	/**
	 * Indicates that the object of the given type and with the indicated ID is exploding.
	 */
	public ExplosionPacket(ObjectType objectType, int objectId) {
		super(getSubType(objectType));

		if (objectType == null) {
			throw new IllegalArgumentException("You must provide an object type");
		}

		if (objectId < 0) {
			throw new IllegalArgumentException("Invalid object ID: " + objectId);
		}

		mObjectType = objectType;
		mObjectId = objectId;
	}

	public ExplosionPacket(PacketReader reader) {
		super(reader);
		mObjectType = ObjectType.fromId(reader.readInt());
		mObjectId = reader.readInt();
	}

	/**
	 * The type of object that is exploding
	 */
	public ObjectType getObjectType() {
		return mObjectType;
	}

	/**
	 * The ID of the exploding object
	 */
	public int getObjectId() {
		return mObjectId;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeInt(mObjectType.getId()).writeInt(mObjectId);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mObjectType).append(" #").append(mObjectId);
	}

	/**
	 * Returns whether the appropriate packet subtype is DETONATION (for MINE or TORPEDO object
	 * types) or EXPLOSION (for other object types).
	 */
	private static byte getSubType(ObjectType type) {
		if (type == ObjectType.MINE || type == ObjectType.TORPEDO) {
			return SubType.DETONATION;
		}

		return SubType.EXPLOSION;
	}
}
