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
    	NAME
    }
	private static final int BIT_COUNT = Bit.values().length;

	OtherParser(ObjectType objectType) {
		super(objectType);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisGenericObject parseImpl(PacketReader reader) {
        final ArtemisGenericObject obj = new ArtemisGenericObject(reader.getObjectId());
        obj.setType(reader.getObjectType());
        obj.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        obj.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        obj.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));
        obj.setName(reader.readString(Bit.NAME)); // generic objects aren't usually named, but custom missions might name them
        return obj;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisGenericObject gObj = (ArtemisGenericObject) obj;
    	writer	.writeFloat(Bit.X, gObj.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, gObj.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, gObj.getZ(), Float.MIN_VALUE)
				.writeString(Bit.NAME, gObj.getName());
	}
}