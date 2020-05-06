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

    	SCAN,
    	UNK_2_2,
    	UNK_2_3,
    	UNK_2_4,
    	UNK_2_5,
    	UNK_2_6,
    	HEALTH,
    	MAX_HEALTH,
    	
    	UNK_3_1,
    	UNK_3_2;
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
        creature.setX(reader.readFloat(Bit.X));
        creature.setY(reader.readFloat(Bit.Y));
        creature.setZ(reader.readFloat(Bit.Z));
		creature.setName(reader.readString(Bit.NAME));
        creature.setHeading(reader.readFloat(Bit.HEADING));
        creature.setPitch(reader.readFloat(Bit.PITCH));
        creature.setRoll(reader.readFloat(Bit.ROLL));

        if (reader.has(Bit.CREATURE_TYPE)) {
            creature.setCreatureType(CreatureType.values()[reader.readInt()]);
        }

        if (reader.has(Bit.SCAN)) {
            creature.setScanBits(reader.readInt(Bit.SCAN, 0));
        }

        reader.readObjectUnknown(Bit.UNK_2_2, 4);
        reader.readObjectUnknown(Bit.UNK_2_3, 4);
        reader.readObjectUnknown(Bit.UNK_2_4, 4);
        reader.readObjectUnknown(Bit.UNK_2_5, 4);
        reader.readObjectUnknown(Bit.UNK_2_6, 4);

        creature.setHealth(reader.readFloat(Bit.HEALTH));
        creature.setMaxHealth(reader.readFloat(Bit.MAX_HEALTH));

        reader.readObjectUnknown(Bit.UNK_3_1, 1);
        reader.readObjectUnknown(Bit.UNK_3_2, 4);
        return creature;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisCreature creature = (ArtemisCreature) obj;
		writer	.writeFloat(Bit.X, creature.getX())
				.writeFloat(Bit.Y, creature.getY())
				.writeFloat(Bit.Z, creature.getZ())
				.writeString(Bit.NAME, creature.getName())
				.writeFloat(Bit.HEADING, creature.getHeading())
				.writeFloat(Bit.PITCH, creature.getPitch())
				.writeFloat(Bit.ROLL, creature.getRoll());

		CreatureType creatureType = creature.getCreatureType();

		if (creatureType != null) {
			writer.writeInt(Bit.CREATURE_TYPE, creatureType.ordinal(), -1);
		}

		Integer scanBits = creature.getScanBits();

		if (scanBits != null) {
		    writer.writeInt(Bit.SCAN, scanBits, 1);
		}

		writer	.writeUnknown(Bit.UNK_2_2)
				.writeUnknown(Bit.UNK_2_3)
				.writeUnknown(Bit.UNK_2_4)
				.writeUnknown(Bit.UNK_2_5)
				.writeUnknown(Bit.UNK_2_6)
				.writeFloat(Bit.HEALTH, creature.getHealth())
				.writeFloat(Bit.MAX_HEALTH, creature.getMaxHealth())
				.writeUnknown(Bit.UNK_3_1)
				.writeUnknown(Bit.UNK_3_2);
	}
}