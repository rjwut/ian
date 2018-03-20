package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Sent by the client to initiate an emergency jump.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.EMERGENCY_JUMP)
public class HelmEmergencyJumpPacket extends ValueIntPacket {
    private static final int FORWARD = 0;
    private static final int BACKWARD = 1;

    public HelmEmergencyJumpPacket(boolean forward) {
    	super(forward ? FORWARD : BACKWARD);
    }

    public HelmEmergencyJumpPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns true if the emergency jump is forward, false if it's backward.
     */
    public boolean isForward() {
    	return mArg == FORWARD;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
    	b.append(isForward() ? "FORWARD" : "BACKWARD");
	}

}
