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
 * Set steering amount. Just like the actual console, you need to send one
 * packet to start turning, then another to reset the steering angle to stop
 * turning.
 * @author dhleong
 */
public class HelmSetSteeringPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.VALUE_FLOAT;
    private static final byte SUBTYPE = 0x01;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return HelmSetSteeringPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new HelmSetSteeringPacket(reader);
			}
		});
	}

    private float mSteering;

    /**
     * @param steering float in [0, 1], where 0.5 is "centered" (no turning),
     * 0.0 is left (hard to port), 1.0 is right (hard to starboard)
     */
    public HelmSetSteeringPacket(float steering) {
        super(ConnectionType.CLIENT, TYPE);

        if (steering < 0 || steering > 1) {
        	throw new IllegalArgumentException("Steering out of range");
        }
        
        mSteering = steering;
    }

    private HelmSetSteeringPacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
        reader.skip(4); // subtype
    	mSteering = reader.readFloat();
    }

    public float getSteering() {
    	return mSteering;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer.writeInt(SUBTYPE).writeFloat(mSteering);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mSteering);
	}
}
