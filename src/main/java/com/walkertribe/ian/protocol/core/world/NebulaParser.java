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
    	BLUE;
    }
	private static final int BIT_COUNT = Bit.values().length;

	NebulaParser() {
		super(ObjectType.NEBULA);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisNebula parseImpl(PacketReader reader) {
        final ArtemisNebula obj = new ArtemisNebula(reader.getObjectId());
        obj.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        obj.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        obj.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));
        obj.setRed(reader.readFloat(Bit.RED, -1));
        obj.setGreen(reader.readFloat(Bit.GREEN, -1));
        obj.setBlue(reader.readFloat(Bit.BLUE, -1));
        return obj;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisNebula nObj = (ArtemisNebula) obj;
    	writer	.writeFloat(Bit.X, nObj.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, nObj.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, nObj.getZ(), Float.MIN_VALUE)
				.writeFloat(Bit.RED, nObj.getRed(), -1)
				.writeFloat(Bit.GREEN, nObj.getGreen(), -1)
				.writeFloat(Bit.BLUE, nObj.getBlue(), -1);
	}
}