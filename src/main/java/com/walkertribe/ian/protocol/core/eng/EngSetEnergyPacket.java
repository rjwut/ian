package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.Artemis;

/**
 * Sets the amount of energy allocated to a system.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_FLOAT, subtype = 0x04)
public class EngSetEnergyPacket extends BaseArtemisPacket {
    private ShipSystem mSystem;
    private float mValue;

    /**
     * Sets the given system's energy allocation.
     * @param system The ShipSystem whose energy allocation is to be set
     * @param value A number between 0 (no energy) and 1 (max possible energy),
     * 		inclusive. A value of 0.333333... is the default allocation level.
     */
    public EngSetEnergyPacket(ShipSystem system, float value) {
        if (system == null) {
        	throw new IllegalArgumentException("You must provide a system");
        }

        if (value < 0) {
        	throw new IllegalArgumentException(
        			"You cannot allocate a negative amount of energy"
        	);
        }

        if (value > 1) {
        	throw new IllegalArgumentException(
        			"You cannot allocate more than " +
        			Artemis.MAX_ENERGY_ALLOCATION_PERCENT + "% energy"
        	);
        }

        mSystem = system;
        mValue = value;
    }

    /**
     * Sets the given system's energy allocation.
     * @param system The ShipSystem whose energy allocation is to be set
     * @param percentage A number between 0 (no energy) and 300 (max possible
     * 		energy), inclusive. This value corresponds to the energy allocation
     * 		percentage as seen in the UI. A value of 100 (100%) is the default
     * 		allocation level.
     */
    public EngSetEnergyPacket(ShipSystem system, int percentage) {
        this(system, percentage / (float) Artemis.MAX_ENERGY_ALLOCATION_PERCENT);
    }

    public EngSetEnergyPacket(PacketReader reader) {
        reader.skip(4); // subtype
    	mValue = reader.readFloat();
    	mSystem = ShipSystem.values()[reader.readInt()];
    }

    /**
     * The ship system whose energy level is being set.
     */
    public ShipSystem getSystem() {
    	return mSystem;
    }

    /**
     * The energy level to set, as a value between 0 and 1 (inclusive), with 0
     * meaning 0% allocation, and 1 meaning 300% allocation.
     */
    public float getAllocation() {
    	return mValue;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer	.writeInt(0x04) // subtype
				.writeFloat(mValue)
				.writeInt(mSystem.ordinal());
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b	.append(mSystem).append(" = ")
			.append(mValue * Artemis.MAX_ENERGY_ALLOCATION_PERCENT).append('%');
	}
}