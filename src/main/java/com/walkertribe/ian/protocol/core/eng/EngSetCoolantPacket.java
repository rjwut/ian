package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.ShipSystem;
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
 * Set the amount of coolant in a system.
 * @author dhleong
 */
public class EngSetCoolantPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.VALUE_FOUR_INTS;
    private static final byte SUBTYPE = 0x00;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return EngSetCoolantPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new EngSetCoolantPacket(reader);
			}
		});
	}

    private ShipSystem mSystem;
    private int mCoolant;

    /**
     * @param system The ShipSystem whose coolant level is to be set
     * @param coolant The amount of coolant to allocate
     */
    public EngSetCoolantPacket(ShipSystem system, int coolant) {
        super(ConnectionType.CLIENT, TYPE);

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

    private EngSetCoolantPacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
        reader.skip(4); // subtype
    	mSystem = ShipSystem.values()[reader.readInt()];
    	mCoolant = reader.readInt();
    }

    public ShipSystem getSystem() {
    	return mSystem;
    }

    public int getCoolant() {
    	return mCoolant;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer	.writeInt(SUBTYPE)
    			.writeInt(mSystem.ordinal())
    			.writeInt(mCoolant);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mSystem).append(" = ").append(mCoolant);
	}
}