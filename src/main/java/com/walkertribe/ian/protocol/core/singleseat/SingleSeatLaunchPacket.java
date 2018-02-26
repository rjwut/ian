package com.walkertribe.ian.protocol.core.singleseat;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Requests that a particular single-seat craft be launched.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.SINGLE_SEAT_LAUNCH)
public class SingleSeatLaunchPacket extends ValueIntPacket {
	/**
	 * Requests the launch of the single-seat craft with the given ID.
	 */
    public SingleSeatLaunchPacket(int objectId) {
        super(objectId);
    }

    public SingleSeatLaunchPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * The object ID of the single-seat craft to launch.
     */
    public int getObjectId() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mArg);
	}
}
