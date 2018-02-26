package com.walkertribe.ian.protocol.core.sci;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Sets the science officer's current target.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.SCIENCE_SELECT)
public class SciTargetPacket extends ValueIntPacket {
	/**
	 * @param target The target to select (or null to clear the target)
	 */
    public SciTargetPacket(ArtemisObject target) {
        super(target == null ? 1 : target.getId());
    }

    public SciTargetPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the ID of the selected target, or 1 if the previously-selected
     * target has been deselected.
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