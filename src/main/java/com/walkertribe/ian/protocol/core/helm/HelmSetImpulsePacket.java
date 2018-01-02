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
 * Set impulse power.
 * @author dhleong
 */
public class HelmSetImpulsePacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.VALUE_FLOAT;
    private static final byte SUBTYPE = 0x00;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return HelmSetImpulsePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new HelmSetImpulsePacket(reader);
			}
		});
	}

    private float mPower;

    /**
     * @param power Impulse power percentage (value between 0 and 1, inclusive)
     */
    public HelmSetImpulsePacket(float power) {
        super(ConnectionType.CLIENT, TYPE);

        if (power < 0 || power > 1) {
        	throw new IllegalArgumentException("Impulse power out of range");
        }

        mPower = power;
    }

    private HelmSetImpulsePacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
        reader.skip(4); // subtype
    	mPower = reader.readFloat();
    }

    public float getPower() {
    	return mPower;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(SUBTYPE).writeFloat(mPower);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mPower * 100).append('%');
	}
}
