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
import com.walkertribe.ian.protocol.UnexpectedTypeException;
import com.walkertribe.ian.world.Artemis;

/**
 * Load a type of torpedo into a tube.
 * @author dhleong
 */
public class LoadTubePacket extends BaseArtemisPacket {
    private static final int TYPE = 0x69CC01D9;
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
     * @param tube Index of tube to load, [0 - Artemis.MAX_TUBES)
     * @param torpedoType OrdnanceType value indicating what to load
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
        int subtype = reader.readInt();

        if (subtype != SUBTYPE) {
        	throw new UnexpectedTypeException(subtype, SUBTYPE);
        }

        mTube = reader.readInt();
        mOrdnanceType = OrdnanceType.values()[reader.readInt()];
        mUnknown0 = reader.readInt();
        mUnknown1 = reader.readInt();
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