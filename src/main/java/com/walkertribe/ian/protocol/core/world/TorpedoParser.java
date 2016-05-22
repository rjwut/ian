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
    	NAME,
    	UNK_1_5,
    	UNK_1_6,
    	UNK_1_7,
    	UNK_1_8
    }
	private static final Bit[] BITS = Bit.values();
	private static final byte[] UNK_TORPEDO = { 0 };

	TorpedoParser() {
		super(ObjectType.TORPEDO);
	}

	@Override
	public Bit[] getBits() {
		return BITS;
	}

	@Override
	protected ArtemisGenericObject parseImpl(PacketReader reader) {
        reader.readObjectUnknown("UNK_TORPEDO", 1);
        float x = reader.readFloat(Bit.X, Float.MIN_VALUE);
        float y = reader.readFloat(Bit.Y, Float.MIN_VALUE);
        float z = reader.readFloat(Bit.Z, Float.MIN_VALUE);
        reader.readObjectUnknown(Bit.NAME, 4);
        final ArtemisGenericObject obj = new ArtemisGenericObject(reader.getObjectId());
        obj.setType(ObjectType.TORPEDO);
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
        writer.writeUnknown("UNK_TORPEDO", UNK_TORPEDO);
    	writer	.writeFloat(Bit.X, gObj.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, gObj.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, gObj.getZ(), Float.MIN_VALUE);
    	writer	.writeUnknown(Bit.NAME)
        		.writeUnknown(Bit.UNK_1_5)
				.writeUnknown(Bit.UNK_1_6)
				.writeUnknown(Bit.UNK_1_7)
				.writeUnknown(Bit.UNK_1_8);
	}
}