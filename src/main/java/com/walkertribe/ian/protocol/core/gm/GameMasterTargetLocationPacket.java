package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Selects a location on the game master's map.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_FLOAT, subtype = 0x06)
public class GameMasterTargetLocationPacket extends BaseArtemisPacket {
    private float mX;
    private float mZ;
    private byte[] mUnknown;

    public GameMasterTargetLocationPacket(float x, float z) {
        mX = x;
        mZ = z;
        mUnknown = new byte[4];
    }

    public GameMasterTargetLocationPacket(PacketReader reader) {
    	reader.skip(4); // subtype
    	mZ = reader.readFloat();
    	mUnknown = reader.readBytes(4);
    	mX = reader.readFloat();
    }

    /**
     * Return the X-coordinate for the selected location.
     */
    public float getX() {
    	return mX;
    }

    /**
     * Return the Z-coordinate for the selected location.
     */
    public float getZ() {
    	return mZ;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer
    		.writeInt(0x06) // subtype
    		.writeFloat(mZ)
    		.writeBytes(mUnknown)
    		.writeFloat(mX);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("x=").append(mX).append(" z=").append(mZ);
	}
}
