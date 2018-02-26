package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Set the target for our weapons.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.WEAPONS_SELECT)
public class WeaponsTargetPacket extends ValueIntPacket {
	/**
	 * @param target The desired target (or null to release target lock)
	 */
    public WeaponsTargetPacket(ArtemisObject target) {
        super(target == null ? 1 : target.getId());
    }

    public WeaponsTargetPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the target's ID.
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