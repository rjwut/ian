package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Set impulse power.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_FLOAT, subtype = 0x00)
public class HelmSetImpulsePacket extends BaseArtemisPacket {
    private float mPower;

    /**
     * @param power Impulse power percentage (value between 0 and 1, inclusive)
     */
    public HelmSetImpulsePacket(float power) {
        if (power < 0 || power > 1) {
        	throw new IllegalArgumentException("Impulse power out of range");
        }

        mPower = power;
    }

    public HelmSetImpulsePacket(PacketReader reader) {
        reader.skip(4); // subtype
    	mPower = reader.readFloat();
    }

    /**
     * The desired impulse power, as a value between 0 and 1 inclusive.
     */
    public float getPower() {
    	return mPower;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
		writer
			.writeInt(0x00) // subtype
			.writeFloat(mPower);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mPower * 100).append('%');
	}
}
