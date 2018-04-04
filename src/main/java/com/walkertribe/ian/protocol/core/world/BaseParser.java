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
		UNK_1_4,
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
        base.setShieldsFront(reader.readFloat(Bit.FORE_SHIELDS));
        base.setShieldsRear(reader.readFloat(Bit.AFT_SHIELDS));
        reader.readObjectUnknown(Bit.UNK_1_4, 4);
        base.setHullId(reader.readInt(Bit.HULL_ID, -1));
        base.setX(reader.readFloat(Bit.X));
        base.setY(reader.readFloat(Bit.Y));
        base.setZ(reader.readFloat(Bit.Z));
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
				.writeFloat(Bit.FORE_SHIELDS, base.getShieldsFront())
				.writeFloat(Bit.AFT_SHIELDS, base.getShieldsRear())
				.writeUnknown(Bit.UNK_1_4)
				.writeInt(Bit.HULL_ID, base.getHullId(), -1)
				.writeFloat(Bit.X, base.getX())
				.writeFloat(Bit.Y, base.getY())
				.writeFloat(Bit.Z, base.getZ())
				.writeUnknown(Bit.UNK_2_1)
				.writeUnknown(Bit.UNK_2_2)
				.writeUnknown(Bit.UNK_2_3)
				.writeUnknown(Bit.UNK_2_4)
				.writeUnknown(Bit.UNK_2_5)
				.writeUnknown(Bit.UNK_2_6);
	}
}