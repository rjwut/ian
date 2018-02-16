package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.OrdnanceType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.Artemis;

/**
 * Load a type of ordnance into a tube.
 * @author dhleong
 */
public class LoadTubePacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.VALUE_FOUR_INTS;
    private static final byte SUBTYPE = 0x02;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return LoadTubePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new LoadTubePacket(reader);
			}
		});
	}

    private int mTube;
    private OrdnanceType mOrdnanceType;
    private int mUnknown0;
    private int mUnknown1;

    /**
     * Loads the given tube with the indicated ordnance.
     */
    public LoadTubePacket(int tube, OrdnanceType ordnanceType) {
        super(ConnectionType.CLIENT, TYPE);

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

    private LoadTubePacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
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
    	writer	.writeInt(SUBTYPE)
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