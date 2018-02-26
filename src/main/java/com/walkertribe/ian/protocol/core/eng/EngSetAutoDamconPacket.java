package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Set whether DAMCON teams should be autonomous or not.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.SET_AUTO_DAMCON)
public class EngSetAutoDamconPacket extends ValueIntPacket {
	/**
	 * @param autonomous Whether DAMCON teams should be autonomous
	 */
    public EngSetAutoDamconPacket(boolean autonomous) {
        super(autonomous ? 1 : 0);
    }

    public EngSetAutoDamconPacket(PacketReader reader) {
        super(reader);
    }

    public boolean isAutonomous() {
    	return mArg == 1;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg == 1 ? "on" : "off");
	}
}