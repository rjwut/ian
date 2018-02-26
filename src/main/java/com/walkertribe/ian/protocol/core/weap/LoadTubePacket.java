package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.OrdnanceType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.Artemis;

/**
 * Load a type of ordnance into a tube.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_FOUR_INTS, subtype = 0x02)
public class LoadTubePacket extends BaseArtemisPacket {
    private int mTube;
    private OrdnanceType mOrdnanceType;
    private int mUnknown0;
    private int mUnknown1;

    /**
     * Loads the given tube with the indicated ordnance.
     */
    public LoadTubePacket(int tube, OrdnanceType ordnanceType) {
        if (tube < 0 || tube >= Artemis.MAX_TUBES) {
        	throw new IndexOutOfBoundsException(
        			"Invalid tube index: " + tube
        	);
        }

        if (ordnanceType == null) {
        	throw new IllegalArgumentException(
        			"You must specify an ordnance type"
        	);
        }

        mTube = tube;
        mOrdnanceType = ordnanceType;
    }

    public LoadTubePacket(PacketReader reader) {
        reader.skip(4); // subtype
        mTube = reader.readInt();
        mOrdnanceType = OrdnanceType.values()[reader.readInt()];
        mUnknown0 = reader.readInt();
        mUnknown1 = reader.readInt();
    }

    /**
     * The index of the tube to load.
     */
    public int getTubeIndex() {
    	return mTube;
    }

    /**
     * The ordnance to load in the tube.
     */
    public OrdnanceType getOrdnanceType() {
    	return mOrdnanceType;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer	.writeInt(0x02) // subtype
    			.writeInt(mTube)
    			.writeInt(mOrdnanceType.ordinal())
    			.writeInt(mUnknown0)
    			.writeInt(mUnknown1);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("Tube #").append(mTube).append(": ").append(mOrdnanceType);
		b.append("\nUnknown: ").append(mUnknown0).append("/").append(mUnknown1);
	}
}