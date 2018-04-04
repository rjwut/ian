package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisGenericObject;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * ObjectParser implementation for mines, black holes, and asteroids
 * @author rjwut
 */
public class OtherParser extends AbstractObjectParser {
	private enum Bit {
    	X,
    	Y,
    	Z
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
        obj.setX(reader.readFloat(Bit.X));
        obj.setY(reader.readFloat(Bit.Y));
        obj.setZ(reader.readFloat(Bit.Z));
        return obj;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisGenericObject gObj = (ArtemisGenericObject) obj;
    	writer	.writeFloat(Bit.X, gObj.getX())
				.writeFloat(Bit.Y, gObj.getY())
				.writeFloat(Bit.Z, gObj.getZ());
	}
}