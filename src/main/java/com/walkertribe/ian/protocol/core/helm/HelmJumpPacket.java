package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Initiate a jump. There is no confirmation; that's all client-side.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_FLOAT, subtype = 0x05)
public class HelmJumpPacket extends BaseArtemisPacket {
    private float mHeading;
    private float mDistance;

    /**
     * Initiates a jump for the indicated direction and distance.
     * @param heading Heading as a percentage of 360
     * @param distance Distance as a percentage of the max possible jump
     * 		distance, 50K
     */
    public HelmJumpPacket(float heading, float distance) {
        if (heading < 0 || heading > 1) {
        	throw new IllegalArgumentException("Heading out of range");
        }

        if (distance < 0 || distance > 1) {
        	throw new IllegalArgumentException("Distance out of range");
        }
        
        mHeading = heading;
        mDistance = distance;
    }

    public HelmJumpPacket(PacketReader reader) {
        reader.skip(4); // subtype
        mHeading = reader.readFloat();
        mDistance = reader.readFloat();
    }

    public float getHeading() {
    	return mHeading;
    }

    public float getDistance() {
    	return mDistance;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer	.writeInt(0x05) // subtype
				.writeFloat(mHeading)
				.writeFloat(mDistance);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("heading = ").append(mHeading * 360)
		.append(" deg; distance = ").append(mDistance * 50).append('k');
	}
}
