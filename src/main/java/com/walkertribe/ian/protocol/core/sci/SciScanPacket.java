package com.walkertribe.ian.protocol.core.sci;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Scans the indicated target.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.SCIENCE_SCAN)
public class SciScanPacket extends ValueIntPacket {
	/**
	 * @param target The target to scan
	 */
    public SciScanPacket(ArtemisObject target) {
        super(target != null ? target.getId() : 0);

        if (target == null) {
        	throw new IllegalArgumentException("You must provide a target");
        }
    }

    public SciScanPacket(PacketReader reader) {
        super(reader);
    }

    /**
     * Returns the ID of the target to be scanned.
     */
    public int getTargetId() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mArg);
	}
}