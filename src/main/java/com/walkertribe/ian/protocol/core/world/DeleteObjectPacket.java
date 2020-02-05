package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Sent by the server when an object is deleted from the simulation. This
 * doesn't necessarily mean that an explosion effect should be shown.
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.OBJECT_DELETE)
public class DeleteObjectPacket extends BaseArtemisPacket {
    private final ObjectType mTargetType;
    private final int mTarget;

    public DeleteObjectPacket(ArtemisObject obj) {
    	this(obj.getType(), obj.getId());
    }

    public DeleteObjectPacket(ObjectType targetType, int id) {
        mTargetType = targetType;
        mTarget = id;
    }

    public DeleteObjectPacket(PacketReader reader) {
    	this(ObjectType.fromId(reader.readByte()), reader.readInt());
    }

    /**
     * The ObjectType of the deleted object
     */
    public ObjectType getTargetType() {
        return mTargetType;
    }

    /**
     * The deleted object's ID
     */
    public int getTarget() {
        return mTarget;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeByte(mTargetType.getId()).writeInt(mTarget);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mTarget).append(" (").append(mTargetType).append(')');
	}
}