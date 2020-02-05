package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisNebula;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * ObjectParser implementation for nebulae
 * @author rjwut
 */
public class NebulaParser extends AbstractObjectParser {
	private enum Bit {
    	X,
    	Y,
    	Z,
    	RED,
    	GREEN,
    	BLUE,
    	TYPE;
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
        obj.setX(reader.readFloat(Bit.X));
        obj.setY(reader.readFloat(Bit.Y));
        obj.setZ(reader.readFloat(Bit.Z));
        obj.setRed(reader.readFloat(Bit.RED));
        obj.setGreen(reader.readFloat(Bit.GREEN));
        obj.setBlue(reader.readFloat(Bit.BLUE));
        obj.setNebulaType(reader.readByte(Bit.TYPE, (byte) -1));
        return obj;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisNebula nObj = (ArtemisNebula) obj;
    	writer	.writeFloat(Bit.X, nObj.getX())
				.writeFloat(Bit.Y, nObj.getY())
				.writeFloat(Bit.Z, nObj.getZ())
				.writeFloat(Bit.RED, nObj.getRed())
				.writeFloat(Bit.GREEN, nObj.getGreen())
				.writeFloat(Bit.BLUE, nObj.getBlue())
				.writeByte(Bit.TYPE, nObj.getNebulaType(), (byte) -1);
	}
}