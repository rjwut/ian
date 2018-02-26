package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.world.Artemis;

/**
 * Fire whatever's in the given tube.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.FIRE_TUBE)
public class FireTubePacket extends ValueIntPacket {
	/**
	 * @param tube The index of the tube to fire, [0 - Artemis.MAX_TUBES)
	 */
    public FireTubePacket(int tube) {
        super(tube);

        if (tube < 0 || tube >= Artemis.MAX_TUBES) {
        	throw new IndexOutOfBoundsException(
        			"Invalid tube index: " + tube
        	);
        }
    }

    public FireTubePacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the index of the tube to fire.
     */
    public int getTubeIndex() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}