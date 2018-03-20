package com.walkertribe.ian.protocol.core.singleseat;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.SINGLE_SEAT_SHOOT)
public class SingleSeatShootPacket extends BaseArtemisPacket {
	private int mShooterId;
	private int mTargetId;

	/**
	 * Requests the launch of the single-seat craft with the given ID.
	 */
    public SingleSeatShootPacket(int shooterId, int targetId) {
    	mShooterId = shooterId;
    	mTargetId = targetId;
    }

    public SingleSeatShootPacket(PacketReader reader) {
    	reader.skip(4); // subtype
    	mShooterId = reader.readInt();
    	mTargetId = reader.readInt();
    }

    /**
     * The object ID of the single-seat craft that is shooting.
     */
    public int getShooterId() {
    	return mShooterId;
    }

    /**
     * The object ID of the object being shot.
     */
    public int getTargetId() {
    	return mTargetId;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer
			.writeInt(SubType.SINGLE_SEAT_SHOOT)
			.writeInt(mShooterId)
			.writeInt(mTargetId);
	}

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mShooterId).append(" => ").append(mTargetId);
	}
}
