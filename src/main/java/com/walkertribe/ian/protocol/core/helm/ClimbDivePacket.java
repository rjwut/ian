package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Changes the ship's trim, causing it to climb, dive or level out.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.CLIMB_DIVE)
public class ClimbDivePacket extends ValueIntPacket {
    private static final int UP = -1;
    private static final int DOWN = 1;

    /**
     * Giving an "up" command while diving causes the ship to level out; giving
     * a second "up" command causes it to start climbing. The "down" command
     * does the reverse.
     * @param up True if you want to tilt the ship up, false to tilt it down.
     */
    public ClimbDivePacket(boolean up) {
        super(up ? UP : DOWN);
    }

    public ClimbDivePacket(PacketReader reader) {
        super(reader);
    }

    /**
     * Returns true if this is an "up" command. (Otherwise, it's a "down"
     * command).
     */
    public boolean isUp() {
    	return mArg == UP;
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg == UP ? "up" : "down");
	}
}
