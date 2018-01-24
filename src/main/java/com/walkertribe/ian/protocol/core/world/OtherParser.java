package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisGenericObject;
import com.walkertribe.ian.world.ArtemisObject;

public class OtherParser extends AbstractObjectParser {
	private enum Bit {
    	X,
    	Y,
    	Z,
    	NAME,
    	UNK_1_5,
    	UNK_1_6,
    	UNK_1_7,
    	UNK_1_8
    }
	private static final Bit[] BITS = Bit.values();

	OtherParser(ObjectType objectType) {
		super(objectType);
	}

	@Override
	public Bit[] getBits() {
		return BITS;
	}

	@Override
	protected ArtemisGenericObject parseImpl(PacketReader reader) {
        final ArtemisGenericObject obj = new ArtemisGenericObject(reader.getObjectId());
        obj.setType(reader.getObjectType());
        obj.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        obj.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        obj.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));
        obj.setName(reader.readString(Bit.NAME)); // generic objects aren't usually named, but custom missions might name them
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
				.writeFloat(Bit.Z, gObj.getZ(), Float.MIN_VALUE)
				.writeString(Bit.NAME, gObj.getName())
				.writeUnknown(Bit.UNK_1_5)
				.writeUnknown(Bit.UNK_1_6)
				.writeUnknown(Bit.UNK_1_7)
				.writeUnknown(Bit.UNK_1_8);
	}
}