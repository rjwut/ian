package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Selects (or deselects) a target on the game master's map.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.GM_SELECT)
public class GameMasterTargetObjectPacket extends ValueIntPacket {
	/**
	 * @param target The target to select, or null to deselect a target
	 */
    public GameMasterTargetObjectPacket(ArtemisObject target) {
        super(target == null ? 1 : target.getId());
    }

    public GameMasterTargetObjectPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the ID of the selected object, or 1 if the previously-selected
     * object was deselected.
     */
    public int getTargetId() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
    	if (mArg == 1) {
    		b.append("DESELECTED");
    	} else {
    		b.append('#').append(mArg);
    	}
	}
}