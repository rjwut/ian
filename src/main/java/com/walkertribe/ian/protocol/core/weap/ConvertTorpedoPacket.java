package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Converts a homing torpedo to energy or vice-versa.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_FOUR_INTS, subtype = 0x03)
public class ConvertTorpedoPacket extends BaseArtemisPacket {
	public enum Direction {
		TORPEDO_TO_ENERGY, ENERGY_TO_TORPEDO
	}

    private Direction mDirection;
    private byte[] mUnknown;

    /**
     * @param direction The Direction value indicating the desired conversion type
     */
    public ConvertTorpedoPacket(final Direction direction) {
        if (direction == null) {
        	throw new IllegalArgumentException("You must specify a direction");
        }

        mDirection = direction;
        mUnknown = new byte[] {
        		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
    }

    public ConvertTorpedoPacket(PacketReader reader) {
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
			.writeInt(0x03) // subtype
			.writeFloat(mDirection.ordinal())
			.writeBytes(mUnknown);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mDirection);
	}
}
