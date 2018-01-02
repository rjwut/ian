package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Set climb/dive.
 */
public class HelmSetClimbDivePacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.VALUE_FLOAT;
    private static final byte SUBTYPE = 0x02;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return HelmSetClimbDivePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new HelmSetClimbDivePacket(reader);
			}
		});
	}

    private float mPitch;

    /**
     * @param pitch steering float in [-1, 1], where 0.0 is "centered" (neither
     * climbing nor diving, 1.0 is hard dive, -1.0 is hard climb
     */
    public HelmSetClimbDivePacket(float pitch) {
        super(ConnectionType.CLIENT, TYPE);

        if (pitch < -1 || pitch > 1) {
        	throw new IllegalArgumentException("Pitch out of range");
        }
        
        mPitch = pitch;
    }

    private HelmSetClimbDivePacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
        reader.skip(4); // subtype
    	mPitch = reader.readFloat();
    }

	public float getPitch() {
		return mPitch;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
    	writer.writeInt(SUBTYPE).writeFloat(mPitch);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mPitch);
	}
}
