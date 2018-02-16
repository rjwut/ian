package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisCreature;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * ObjectParser implementation for creatures
 * @author rjwut
 */
public class CreatureParser extends AbstractObjectParser {
	private enum Bit {
    	X,
    	Y,
    	Z,
    	NAME,
    	HEADING,
    	PITCH,
    	ROLL,
    	CREATURE_TYPE,

    	UNK_2_1,
    	UNK_2_2,
    	UNK_2_3,
    	UNK_2_4,
    	UNK_2_5,
    	UNK_2_6,
    	UNK_2_7,
    	UNK_2_8;
    }
    private static final int BIT_COUNT = Bit.values().length;

    CreatureParser() {
		super(ObjectType.CREATURE);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisCreature parseImpl(PacketReader reader) {
        final ArtemisCreature creature = new ArtemisCreature(reader.getObjectId());
        creature.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        creature.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        creature.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));
		creature.setName(reader.readString(Bit.NAME));
        creature.setHeading(reader.readFloat(Bit.HEADING, Float.MIN_VALUE));
        creature.setPitch(reader.readFloat(Bit.PITCH, Float.MIN_VALUE));
        creature.setRoll(reader.readFloat(Bit.ROLL, Float.MIN_VALUE));

        if (reader.has(Bit.CREATURE_TYPE)) {
            creature.setCreatureType(CreatureType.values()[reader.readInt()]);
        }

        reader.readObjectUnknown(Bit.UNK_2_1, 4);
        reader.readObjectUnknown(Bit.UNK_2_2, 4);
        reader.readObjectUnknown(Bit.UNK_2_3, 4);
        reader.readObjectUnknown(Bit.UNK_2_4, 4);
        reader.readObjectUnknown(Bit.UNK_2_5, 4);
        reader.readObjectUnknown(Bit.UNK_2_6, 4);
        reader.readObjectUnknown(Bit.UNK_2_7, 4);
        reader.readObjectUnknown(Bit.UNK_2_8, 4);
        return creature;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisCreature creature = (ArtemisCreature) obj;
		writer	.writeFloat(Bit.X, creature.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, creature.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, creature.getZ(), Float.MIN_VALUE)
				.writeString(Bit.NAME, creature.getName())
				.writeFloat(Bit.HEADING, creature.getHeading(), Float.MIN_VALUE)
				.writeFloat(Bit.PITCH, creature.getPitch(), Float.MIN_VALUE)
				.writeFloat(Bit.ROLL, creature.getRoll(), Float.MIN_VALUE);

		CreatureType creatureType = creature.getCreatureType();

		if (creatureType != null) {
			writer.writeInt(Bit.CREATURE_TYPE, creatureType.ordinal(), -1);
		}

		writer	.writeUnknown(Bit.UNK_2_1)
				.writeUnknown(Bit.UNK_2_2)
				.writeUnknown(Bit.UNK_2_3)
				.writeUnknown(Bit.UNK_2_4)
				.writeUnknown(Bit.UNK_2_5)
				.writeUnknown(Bit.UNK_2_6)
				.writeUnknown(Bit.UNK_2_7)
				.writeUnknown(Bit.UNK_2_8);
	}
}