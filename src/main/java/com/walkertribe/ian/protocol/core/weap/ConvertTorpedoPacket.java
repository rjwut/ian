package com.walkertribe.ian.protocol.core.weap;

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
 * Converts a homing torpedo to energy or vice-versa.
 */
public class ConvertTorpedoPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.VALUE_FOUR_INTS;
    private static final byte SUBTYPE = 0x03;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return ConvertTorpedoPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new ConvertTorpedoPacket(reader);
			}
		});
	}

	public enum Direction {
		TORPEDO_TO_ENERGY, ENERGY_TO_TORPEDO
	}

    private Direction mDirection;
    private byte[] mUnknown;

    /**
     * @param direction The Direction value indicating the desired conversion type
     */
    public ConvertTorpedoPacket(final Direction direction) {
        super(ConnectionType.CLIENT, TYPE);

        if (direction == null) {
        	throw new IllegalArgumentException("You must specify a direction");
        }

        mDirection = direction;
        mUnknown = new byte[] {
        		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
    }

    private ConvertTorpedoPacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
        reader.skip(4); // subtype
        mDirection = Direction.values()[(int) reader.readFloat()];
        mUnknown = reader.readBytes(12);
    }

    /**
     * Returns the direction of the conversion.
     */
    public Direction getDirection() {
    	return mDirection;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
		writer
			.writeInt(SUBTYPE)
			.writeFloat(mDirection.ordinal())
			.writeBytes(mUnknown);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mDirection);
	}
}
