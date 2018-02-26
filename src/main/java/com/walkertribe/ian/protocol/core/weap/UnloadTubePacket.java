package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.world.Artemis;

/**
 * Unloads the indicated tube.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.UNLOAD_TUBE)
public class UnloadTubePacket extends ValueIntPacket {
	/**
	 * @param tube Index of the tube to unload, [0 - Artemis.MAX_TUBES)
	 */
    public UnloadTubePacket(int tube) {
        super(tube);

        if (tube < 0 || tube >= Artemis.MAX_TUBES) {
        	throw new IndexOutOfBoundsException(
        			"Invalid tube index: " + tube
        	);
        }
    }

    public UnloadTubePacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the index of the tube to be unloaded.
     */
    public int getTubeIndex() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}