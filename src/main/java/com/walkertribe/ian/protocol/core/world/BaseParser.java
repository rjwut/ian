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
		FORE_SHIELDS,
		AFT_SHIELDS,
		INDEX,
		HULL_ID,
		X,
		Y,
		Z,

		UNK_2_1,
		UNK_2_2,
		UNK_2_3,
		UNK_2_4,
		UNK_2_5,
		UNK_2_6
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
        base.setShieldsFront(reader.readFloat(Bit.FORE_SHIELDS, Float.MIN_VALUE));
        base.setShieldsRear(reader.readFloat(Bit.AFT_SHIELDS, Float.MIN_VALUE));
        base.setIndex(reader.readInt(Bit.INDEX, -1));
        base.setHullId(reader.readInt(Bit.HULL_ID, -1));
        base.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        base.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        base.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));
        reader.readObjectUnknown(Bit.UNK_2_1, 4);
        reader.readObjectUnknown(Bit.UNK_2_2, 4);
        reader.readObjectUnknown(Bit.UNK_2_3, 4);
        reader.readObjectUnknown(Bit.UNK_2_4, 4);
        reader.readObjectUnknown(Bit.UNK_2_5, 1);
        reader.readObjectUnknown(Bit.UNK_2_6, 1);
        return base;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisBase base = (ArtemisBase) obj;
		writer	.writeString(Bit.NAME, base.getName())
				.writeFloat(Bit.FORE_SHIELDS, base.getShieldsFront(), Float.MIN_VALUE)
				.writeFloat(Bit.AFT_SHIELDS, base.getShieldsRear(), Float.MIN_VALUE)
				.writeInt(Bit.INDEX, base.getIndex(), -1)
				.writeInt(Bit.HULL_ID, base.getHullId(), -1)
				.writeFloat(Bit.X, base.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, base.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, base.getZ(), Float.MIN_VALUE)
				.writeUnknown(Bit.UNK_2_1)
				.writeUnknown(Bit.UNK_2_2)
				.writeUnknown(Bit.UNK_2_3)
				.writeUnknown(Bit.UNK_2_4)
				.writeUnknown(Bit.UNK_2_5)
				.writeUnknown(Bit.UNK_2_6);
	}
}