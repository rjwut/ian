package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisGenericObject;
import com.walkertribe.ian.world.ArtemisObject;

public class TorpedoParser extends AbstractObjectParser {
	private enum Bit {
    	X,
    	Y,
    	Z,
    	UNK_1_4,
    	UNK_1_5,
    	UNK_1_6,
    	UNK_1_7,
    	UNK_1_8,
    	UNK_2_1
    }
	private static final Bit[] BITS = Bit.values();

	TorpedoParser() {
		super(ObjectType.TORPEDO);
	}

	@Override
	public Bit[] getBits() {
		return BITS;
	}

	@Override
	protected ArtemisGenericObject parseImpl(PacketReader reader) {
        final ArtemisGenericObject obj = new ArtemisGenericObject(reader.getObjectId());
        obj.setType(ObjectType.TORPEDO);
        obj.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        obj.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        obj.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));
        reader.readObjectUnknown(Bit.UNK_1_4, 4);
        reader.readObjectUnknown(Bit.UNK_1_5, 4);
        reader.readObjectUnknown(Bit.UNK_1_6, 4);
        reader.readObjectUnknown(Bit.UNK_1_7, 4);
        reader.readObjectUnknown(Bit.UNK_1_8, 4);
        return obj;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisGenericObject gObj = (ArtemisGenericObject) obj;
    	writer	.writeFloat(Bit.X, gObj.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, gObj.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, gObj.getZ(), Float.MIN_VALUE);
    	writer	.writeUnknown(Bit.UNK_1_4)
        		.writeUnknown(Bit.UNK_1_5)
				.writeUnknown(Bit.UNK_1_6)
				.writeUnknown(Bit.UNK_1_7)
				.writeUnknown(Bit.UNK_1_8);
	}
}