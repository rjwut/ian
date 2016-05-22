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
        ObjectType type = reader.getObjectType();
        float x = reader.readFloat(Bit.X, Float.MIN_VALUE);
        float y = reader.readFloat(Bit.Y, Float.MIN_VALUE);
        float z = reader.readFloat(Bit.Z, Float.MIN_VALUE);
        final ArtemisGenericObject obj = new ArtemisGenericObject(reader.getObjectId());
        String name;

        if (type.isNamed()) {
            name = reader.readString(Bit.NAME);
        } else {
            name = null;
            reader.readObjectUnknown(Bit.NAME, 4);
        }

        obj.setName(name);
        obj.setType(type);
        obj.setX(x);
        obj.setY(y);
        obj.setZ(z);
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

        if (objectType.isNamed()) {
        	writer.writeString(Bit.NAME, gObj.getName());
        } else {
        	writer.writeUnknown(Bit.NAME);
        }

        writer	.writeUnknown(Bit.UNK_1_5)
				.writeUnknown(Bit.UNK_1_6)
				.writeUnknown(Bit.UNK_1_7)
				.writeUnknown(Bit.UNK_1_8);
	}
}