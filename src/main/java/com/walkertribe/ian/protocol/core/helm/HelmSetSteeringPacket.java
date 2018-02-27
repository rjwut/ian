package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Set steering amount. Just like the actual console, you need to send one
 * packet to start turning, then another to reset the steering angle to stop
 * turning.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_FLOAT, subtype = 0x01)
public class HelmSetSteeringPacket extends BaseArtemisPacket {
    private float mSteering;

    /**
     * @param steering float in [0, 1], where 0.5 is "centered" (no turning),
     * 0.0 is left (hard to port), 1.0 is right (hard to starboard)
     */
    public HelmSetSteeringPacket(float steering) {
        if (steering < 0 || steering > 1) {
        	throw new IllegalArgumentException("Steering out of range");
        }
        
        mSteering = steering;
    }

    public HelmSetSteeringPacket(PacketReader reader) {
        reader.skip(4); // subtype
    	mSteering = reader.readFloat();
    }

    /**
     * The desired rudder position, where 0 is hard to port, 0.5 is rudder
     * amidships, and 1.0 is hard to starboard.
     */
    public float getSteering() {
    	return mSteering;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer
    		.writeInt(0x01) // subtype
    		.writeFloat(mSteering);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mSteering);
	}
}
