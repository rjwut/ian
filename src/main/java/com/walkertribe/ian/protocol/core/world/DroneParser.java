package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisDrone;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * ObjectParser implementation for Torgoth drones
 * @author rjwut
 */
public class DroneParser extends AbstractObjectParser {
    private enum Bit {
    	UNK_1_1,
    	X,
    	UNK_1_3,
    	Z,
    	UNK_1_5,
    	Y,
    	HEADING,
    	SIDE,

    	UNK_2_1
    }
    private static final int BIT_COUNT = Bit.values().length;

    DroneParser() {
		super(ObjectType.DRONE);
    }

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisDrone parseImpl(PacketReader reader) {
        final ArtemisDrone drone = new ArtemisDrone(reader.getObjectId());
    	reader.readObjectUnknown(Bit.UNK_1_1, 4);
    	drone.setX(reader.readFloat(Bit.X));
    	reader.readObjectUnknown(Bit.UNK_1_3, 4);
    	drone.setZ(reader.readFloat(Bit.Z));
    	reader.readObjectUnknown(Bit.UNK_1_5, 4);
    	drone.setY(reader.readFloat(Bit.Y));
    	drone.setHeading(reader.readFloat(Bit.HEADING));
    	drone.setSide(reader.readInt(Bit.SIDE, -1));
    	reader.readObjectUnknown(Bit.UNK_2_1, 4);
        return drone;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisDrone drone = (ArtemisDrone) obj;
		writer	.writeUnknown(Bit.UNK_1_1)
				.writeFloat(Bit.X, drone.getX())
				.writeUnknown(Bit.UNK_1_3)
				.writeFloat(Bit.Z, drone.getZ())
				.writeUnknown(Bit.UNK_1_5)
				.writeFloat(Bit.Y, drone.getY())
				.writeFloat(Bit.HEADING, drone.getHeading())
				.writeInt(Bit.SIDE, drone.getSide(), -1)
				.writeUnknown(Bit.UNK_2_1);
	}
}