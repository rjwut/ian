package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.world.Artemis;

/**
 * Set warp speed.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.WARP)
public class HelmSetWarpPacket extends ValueIntPacket {
	/**
	 * @param warp Value between 0 (no warp) and 4 (max warp)
	 */
    public HelmSetWarpPacket(int warp) {
        super(warp);

        if (warp < 0 || warp > Artemis.MAX_WARP) {
        	throw new IllegalArgumentException("Warp speed out of range");
        }
    }

    public HelmSetWarpPacket(PacketReader reader) {
        super(reader);
    }

    /**
     * Returns the desired warp factor, between 0 and 4 inclusive.
     */
    public int getWarpFactor() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}