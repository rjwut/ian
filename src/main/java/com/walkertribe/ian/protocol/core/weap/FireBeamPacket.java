package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Manually fire beams.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.BEAM_REQUEST)
public class FireBeamPacket extends BaseArtemisPacket {
	private int mId;
	private float mX;
	private float mY;
	private float mZ;

    /**
     * Fire at the given target. The x, y and z parameters indicate the
     * coordinates on the target at which to fire.
     */
    public FireBeamPacket(ArtemisObject target, float x, float y, float z) {
        mId = target.getId();
        mX = x;
        mY = y;
        mZ = z;
    }

    public FireBeamPacket(PacketReader reader) {
        mId = reader.readInt();
        mX = reader.readFloat();
        mY = reader.readFloat();
        mZ = reader.readFloat();
    }

    /**
     * Returns the ID of the beam target.
     */
    public int getTargetId() {
    	return mId;
    }

    /**
     * Returns the X-coordinate of the beam endpoint relative to the target's
     * model origin.
     */
    public float getX() {
    	return mX;
    }

    /**
     * Returns the Y-coordinate of the beam endpoint relative to the target's
     * model origin.
     */
    public float getY() {
    	return mY;
    }

    /**
     * Returns the Z-coordinate of the beam endpoint relative to the target's
     * model origin.
     */
    public float getZ() {
    	return mZ;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mId).writeFloat(mX).writeFloat(mY).writeFloat(mZ);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#')
		.append(mId)
		.append(": ")
		.append(mX)
		.append(',')
		.append(mY)
		.append(',')
		.append(mZ);
	}
}