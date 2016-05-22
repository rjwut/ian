package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisNebula;
import com.walkertribe.ian.world.ArtemisObject;

public class NebulaParser extends AbstractObjectParser {
	private enum Bit {
    	X,
    	Y,
    	Z,
    	RED,
    	GREEN,
    	BLUE,
    	UNK_1_7,
    	UNK_1_8
    }
	private static final Bit[] BITS = Bit.values();

	NebulaParser() {
		super(ObjectType.NEBULA);
	}

	@Override
	public Bit[] getBits() {
		return BITS;
	}

	@Override
	protected ArtemisNebula parseImpl(PacketReader reader) {
        final ArtemisNebula obj = new ArtemisNebula(reader.getObjectId());
		reader.readObjectUnknown(Bit.UNK_1_7, 4);
		reader.readObjectUnknown(Bit.UNK_1_8, 4);
        obj.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        obj.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        obj.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));
        obj.setARGB(
        		0,
        		reader.readFloat(Bit.RED, Float.MIN_VALUE),
        		reader.readFloat(Bit.GREEN, Float.MIN_VALUE),
        		reader.readFloat(Bit.BLUE, Float.MIN_VALUE)
        );
        return obj;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisNebula nObj = (ArtemisNebula) obj;
    	writer	.writeFloat(Bit.X, nObj.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, nObj.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, nObj.getZ(), Float.MIN_VALUE)
				.writeFloat(Bit.RED, nObj.getRed() / 255f, Float.MIN_VALUE)
				.writeFloat(Bit.GREEN, nObj.getGreen() / 255f, Float.MIN_VALUE)
				.writeFloat(Bit.BLUE, nObj.getBlue() / 255f, Float.MIN_VALUE)
				.writeUnknown(Bit.UNK_1_7)
				.writeUnknown(Bit.UNK_1_8);
	}
}