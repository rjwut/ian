package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisBase;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * ObjectParser implementation for bases
 * @author rjwut
 */
public class BaseParser extends AbstractObjectParser {
	private enum Bit {
		NAME,
		SHIELDS,
		MAX_SHIELDS,
		UNK_1_4,
		HULL_ID,
		X,
		Y,
		Z,

		PITCH,
		ROLL,
		HEADING,
		UNK_2_4,
		UNK_2_5,
		SIDE
	}
	private static final int BIT_COUNT = Bit.values().length;

	BaseParser() {
		super(ObjectType.BASE);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisBase parseImpl(PacketReader reader) {
        ArtemisBase base = new ArtemisBase(reader.getObjectId());
        base.setName(reader.readString(Bit.NAME));
        base.setShieldsFront(reader.readFloat(Bit.SHIELDS));
        base.setShieldsFrontMax(reader.readFloat(Bit.MAX_SHIELDS));
        reader.readObjectUnknown(Bit.UNK_1_4, 4);
        base.setHullId(reader.readInt(Bit.HULL_ID, -1));
        base.setX(reader.readFloat(Bit.X));
        base.setY(reader.readFloat(Bit.Y));
        base.setZ(reader.readFloat(Bit.Z));
        base.setPitch(reader.readFloat(Bit.PITCH));
        base.setRoll(reader.readFloat(Bit.ROLL));
        base.setHeading(reader.readFloat(Bit.HEADING));
        reader.readObjectUnknown(Bit.UNK_2_4, 4);
        reader.readObjectUnknown(Bit.UNK_2_5, 1);
        base.setSide(reader.readByte(Bit.SIDE, (byte) -1));
        return base;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisBase base = (ArtemisBase) obj;
		writer	.writeString(Bit.NAME, base.getName())
				.writeFloat(Bit.SHIELDS, base.getShieldsFront())
				.writeFloat(Bit.MAX_SHIELDS, base.getShieldsFrontMax())
				.writeUnknown(Bit.UNK_1_4)
				.writeInt(Bit.HULL_ID, base.getHullId(), -1)
				.writeFloat(Bit.X, base.getX())
				.writeFloat(Bit.Y, base.getY())
				.writeFloat(Bit.Z, base.getZ())
				.writeFloat(Bit.PITCH, base.getPitch())
				.writeFloat(Bit.ROLL, base.getRoll())
				.writeFloat(Bit.HEADING, base.getHeading())
				.writeUnknown(Bit.UNK_2_4)
				.writeUnknown(Bit.UNK_2_5)
				.writeByte(Bit.SIDE, base.getSide(), (byte) -1);
	}
}