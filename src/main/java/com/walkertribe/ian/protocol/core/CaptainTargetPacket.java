package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Selects (or deselects) a target on the captain's map.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.CAPTAIN_SELECT)
public class CaptainTargetPacket extends ValueIntPacket {
	/**
	 * @param target The target to select, or null to deselect a target
	 */
    public CaptainTargetPacket(ArtemisObject target) {
        super(target == null ? 1 : target.getId());
    }

    public CaptainTargetPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the ID of the selected target, or 1 if the previous target was
     * deselected.
     */
    public int getTargetId() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
    	if (mArg == 1) {
    		b.append("no target");
    	} else {
    		b.append('#').append(mArg);
    	}
	}
}