package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Set the amount of coolant in a system.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_FOUR_INTS, subtype = 0x00)
public class EngSetCoolantPacket extends BaseArtemisPacket {
    private ShipSystem mSystem;
    private int mCoolant;

    /**
     * @param system The ShipSystem whose coolant level is to be set
     * @param coolant The amount of coolant to allocate
     */
    public EngSetCoolantPacket(ShipSystem system, int coolant) {
        if (system == null) {
        	throw new IllegalArgumentException("You must provide a system");
        }

        if (coolant < 0) {
        	throw new IllegalArgumentException(
        			"You cannot allocate a negative amount of coolant"
        	);
        }

        mSystem = system;
        mCoolant = coolant;
    }

    public EngSetCoolantPacket(PacketReader reader) {
        reader.skip(4); // subtype
    	mSystem = ShipSystem.values()[reader.readInt()];
    	mCoolant = reader.readInt();
    }

    /**
     * The ship system whose coolant level is being set.
     */
    public ShipSystem getSystem() {
    	return mSystem;
    }

    /**
     * The amount of coolant to allocate to this ship system.
     */
    public int getCoolant() {
    	return mCoolant;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer	.writeInt(0x00) // subtype
    			.writeInt(mSystem.ordinal())
    			.writeInt(mCoolant);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mSystem).append(" = ").append(mCoolant);
	}
}