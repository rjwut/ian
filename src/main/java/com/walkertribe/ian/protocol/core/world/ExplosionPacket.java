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

@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.EXPLOSION)
public class ExplosionPacket extends SimpleEventPacket {
	private ObjectType mObjectType;
	private int mObjectId;

	public ExplosionPacket(ArtemisObject obj) {
		this(obj.getType(), obj.getId());
	}

	public ExplosionPacket(ObjectType objectType, int objectId) {
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
	}

	public ObjectType getObjectType() {
		return mObjectType;
	}

	public int getObjectId() {
		return mObjectId;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeByte((byte) mObjectType.getId()).writeInt(mObjectId);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mObjectType).append(" #").append(mObjectId);
	}

}
