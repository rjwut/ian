package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.OrdnanceType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisTorpedo;

public class TorpedoParser extends AbstractObjectParser {
	private enum Bit {
    	X,
    	Y,
    	Z,
    	DELTA_X,
    	DELTA_Y,
    	DELTA_Z,
    	UNK_1_7,
    	ORDNANCE_TYPE
    }
	private static final int BIT_COUNT = Bit.values().length;

	TorpedoParser() {
		super(ObjectType.TORPEDO);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisTorpedo parseImpl(PacketReader reader) {
        final ArtemisTorpedo obj = new ArtemisTorpedo(reader.getObjectId());
        obj.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        obj.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        obj.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));
        obj.setDx(reader.readFloat(Bit.DELTA_X, Float.MIN_VALUE));
        obj.setDy(reader.readFloat(Bit.DELTA_Y, Float.MIN_VALUE));
        obj.setDz(reader.readFloat(Bit.DELTA_Z, Float.MIN_VALUE));
        reader.readObjectUnknown(Bit.UNK_1_7, 4);

        if (reader.has(Bit.ORDNANCE_TYPE)) {
            obj.setOrdnanceType(OrdnanceType.values()[reader.readInt()]);
        }

        return obj;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisTorpedo t = (ArtemisTorpedo) obj;
    	writer	.writeFloat(Bit.X, t.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, t.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, t.getZ(), Float.MIN_VALUE)
				.writeFloat(Bit.DELTA_X, t.getDx(), Float.MIN_VALUE)
				.writeFloat(Bit.DELTA_Y, t.getDy(), Float.MIN_VALUE)
				.writeFloat(Bit.DELTA_Z, t.getDz(), Float.MIN_VALUE)
				.writeUnknown(Bit.UNK_1_7);

    	OrdnanceType ordType = t.getOrdnanceType();

    	if (ordType != null) {
        	writer.writeInt(Bit.ORDNANCE_TYPE, ordType.ordinal(), -1);
    	}
	}
}