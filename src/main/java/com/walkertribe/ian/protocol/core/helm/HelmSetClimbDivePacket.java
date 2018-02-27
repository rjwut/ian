package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Changes the ship's trim, causing it to climb, dive or level out.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_FLOAT, subtype = 0x02)
public class HelmSetClimbDivePacket extends BaseArtemisPacket {
    private float mPitch;

    /**
     * @param pitch steering float in [-1, 1], where 0.0 is "centered" (neither
     * climbing nor diving, 1.0 is hard dive, -1.0 is hard climb
     */
    public HelmSetClimbDivePacket(float pitch) {

        if (pitch < -1 || pitch > 1) {
        	throw new IllegalArgumentException("Pitch out of range");
        }
        
        mPitch = pitch;
    }

    public HelmSetClimbDivePacket(PacketReader reader) {
        reader.skip(4); // subtype
    	mPitch = reader.readFloat();
    }

    /**
     * The desired pitch, as a value between -1 ("hard climb") and 1 ("hard
     * dive").
     */
	public float getPitch() {
		return mPitch;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
    	writer
    		.writeInt(0x02) // subtype
    		.writeFloat(mPitch);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mPitch);
	}
}
