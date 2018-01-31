package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisNpc;
import com.walkertribe.ian.world.ArtemisObject;

public class NpcShipParser extends AbstractObjectParser {
	private enum Bit {
		NAME,
		IMPULSE,
		RUDDER,
		MAX_IMPULSE,
		MAX_TURN_RATE,
		IS_ENEMY,
		SHIP_TYPE,
		X,

		Y,
		Z,
		PITCH,
		ROLL,
		HEADING,
		VELOCITY,
		SURRENDERED,
		UNK_2_8,

		FORE_SHIELD,
		FORE_SHIELD_MAX,
		AFT_SHIELD,
		AFT_SHIELD_MAX,
		UNK_3_5,
		FLEET_NUMBER,
		SPECIAL_ABILITIES,
		SPECIAL_STATE,

		SCAN_LEVEL,
		UNK_4_2,
		UNK_4_3,
		UNK_4_4,
		UNK_4_5,
		UNK_4_6,
		UNK_4_7,
		UNK_4_8,

		UNK_5_1,
		UNK_5_2,
		BEAM_SYSTEM_DAMAGE,
		TORPEDO_SYSTEM_DAMAGE,
		SENSOR_SYSTEM_DAMAGE,
		MANEUVER_SYSTEM_DAMAGE,
		IMPULSE_SYSTEM_DAMAGE,
		WARP_SYSTEM_DAMAGE,

		FORE_SHIELD_SYSTEM_DAMAGE,
		AFT_SHIELD_SYSTEM_DAMAGE,
		SHIELD_FREQUENCY_A,
		SHIELD_FREQUENCY_B,
		SHIELD_FREQUENCY_C,
		SHIELD_FREQUENCY_D,
		SHIELD_FREQUENCY_E
	}
	private static final int BIT_COUNT = Bit.values().length;

	private static final Bit[] SYSTEM_DAMAGES = new Bit[] {
		Bit.BEAM_SYSTEM_DAMAGE,
		Bit.TORPEDO_SYSTEM_DAMAGE,
		Bit.SENSOR_SYSTEM_DAMAGE,
		Bit.MANEUVER_SYSTEM_DAMAGE,
		Bit.IMPULSE_SYSTEM_DAMAGE,
		Bit.WARP_SYSTEM_DAMAGE,
		Bit.FORE_SHIELD_SYSTEM_DAMAGE,
		Bit.AFT_SHIELD_SYSTEM_DAMAGE
	};

	private static final Bit[] SHLD_FREQS = new Bit[] {
		Bit.SHIELD_FREQUENCY_A,
		Bit.SHIELD_FREQUENCY_B,
		Bit.SHIELD_FREQUENCY_C,
		Bit.SHIELD_FREQUENCY_D,
		Bit.SHIELD_FREQUENCY_E
    };

	NpcShipParser() {
		super(ObjectType.NPC_SHIP);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisNpc parseImpl(PacketReader reader) {
        ArtemisNpc obj = new ArtemisNpc(reader.getObjectId());
        obj.setName(reader.readString(Bit.NAME));
        obj.setImpulse(reader.readFloat(Bit.IMPULSE, -1));
        obj.setSteering(reader.readFloat(Bit.RUDDER, -1));
        obj.setTopSpeed(reader.readFloat(Bit.MAX_IMPULSE, -1));
        obj.setTurnRate(reader.readFloat(Bit.MAX_TURN_RATE, -1));
        obj.setEnemy(reader.readBool(Bit.IS_ENEMY, 4));
        obj.setHullId(reader.readInt(Bit.SHIP_TYPE, -1));
        obj.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        obj.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        obj.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));
        obj.setPitch(reader.readFloat(Bit.PITCH, Float.MIN_VALUE));
        obj.setRoll(reader.readFloat(Bit.ROLL, Float.MIN_VALUE));
        obj.setHeading(reader.readFloat(Bit.HEADING, Float.MIN_VALUE));
        obj.setVelocity(reader.readFloat(Bit.VELOCITY, -1));
        obj.setSurrendered(reader.readBool(Bit.SURRENDERED, 1));

        reader.readObjectUnknown(Bit.UNK_2_8, 2);

        obj.setShieldsFront(reader.readFloat(Bit.FORE_SHIELD, Float.MIN_VALUE));
        obj.setShieldsFrontMax(reader.readFloat(Bit.FORE_SHIELD_MAX, -1));
        obj.setShieldsRear(reader.readFloat(Bit.AFT_SHIELD, Float.MIN_VALUE));
        obj.setShieldsRearMax(reader.readFloat(Bit.AFT_SHIELD_MAX, -1));

        reader.readObjectUnknown(Bit.UNK_3_5, 2);

        obj.setFleetNumber(reader.readByte(Bit.FLEET_NUMBER, (byte) -1));
        obj.setSpecialBits(reader.readInt(Bit.SPECIAL_ABILITIES, -1));
        obj.setSpecialStateBits(reader.readInt(Bit.SPECIAL_STATE, -1));
        obj.setScanLevel(reader.readInt(Bit.SCAN_LEVEL, -1));

        reader.readObjectUnknown(Bit.UNK_4_2, 4);
        reader.readObjectUnknown(Bit.UNK_4_3, 4);
        reader.readObjectUnknown(Bit.UNK_4_4, 1);
        reader.readObjectUnknown(Bit.UNK_4_5, 1);
        reader.readObjectUnknown(Bit.UNK_4_6, 1);
        reader.readObjectUnknown(Bit.UNK_4_7, 1);
        reader.readObjectUnknown(Bit.UNK_4_8, 4);
        reader.readObjectUnknown(Bit.UNK_5_1, 4);
        reader.readObjectUnknown(Bit.UNK_5_2, 4);

        // system damage
        for (ShipSystem sys : ShipSystem.values()) {
        	obj.setSystemDamage(sys, reader.readFloat(SYSTEM_DAMAGES[sys.ordinal()], -1));
        }

        // shield frequencies
        for (BeamFrequency freq : BeamFrequency.values()) {
        	obj.setShieldFreq(freq, reader.readFloat(SHLD_FREQS[freq.ordinal()], -1));
        }

        return obj;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisNpc npc = (ArtemisNpc) obj;
		writer	.writeString(Bit.NAME, npc.getName())
				.writeFloat(Bit.IMPULSE, npc.getImpulse(), -1)
				.writeFloat(Bit.RUDDER, npc.getSteering(), -1)
				.writeFloat(Bit.MAX_IMPULSE, npc.getTopSpeed(), -1)
				.writeFloat(Bit.MAX_TURN_RATE, npc.getTurnRate(), -1)
				.writeBool(Bit.IS_ENEMY, npc.isEnemy(), 4)
				.writeInt(Bit.SHIP_TYPE, npc.getHullId(), -1)
				.writeFloat(Bit.X, npc.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, npc.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, npc.getZ(), Float.MIN_VALUE)
				.writeFloat(Bit.PITCH, npc.getPitch(), Float.MIN_VALUE)
				.writeFloat(Bit.ROLL, npc.getRoll(), Float.MIN_VALUE)
				.writeFloat(Bit.HEADING, npc.getHeading(), Float.MIN_VALUE)
				.writeFloat(Bit.VELOCITY, npc.getVelocity(), -1)
				.writeBool(Bit.SURRENDERED, npc.isSurrendered(), 1)
				.writeUnknown(Bit.UNK_2_8)
				.writeFloat(Bit.FORE_SHIELD, npc.getShieldsFront(), Float.MIN_VALUE)
				.writeFloat(Bit.FORE_SHIELD_MAX, npc.getShieldsFrontMax(), -1)
				.writeFloat(Bit.AFT_SHIELD, npc.getShieldsRear(), Float.MIN_VALUE)
				.writeFloat(Bit.AFT_SHIELD_MAX, npc.getShieldsRearMax(), -1)
				.writeUnknown(Bit.UNK_3_5)
				.writeByte(Bit.FLEET_NUMBER, npc.getFleetNumber(), (byte) -1)
				.writeInt(Bit.SPECIAL_ABILITIES, npc.getSpecialBits(), -1)
				.writeInt(Bit.SPECIAL_STATE, npc.getSpecialStateBits(), -1)
				.writeInt(Bit.SCAN_LEVEL, npc.getScanLevel(), -1)
				.writeUnknown(Bit.UNK_4_2)
				.writeUnknown(Bit.UNK_4_3)
				.writeUnknown(Bit.UNK_4_4)
				.writeUnknown(Bit.UNK_4_5)
				.writeUnknown(Bit.UNK_4_6)
				.writeUnknown(Bit.UNK_4_7)
				.writeUnknown(Bit.UNK_4_8)
				.writeUnknown(Bit.UNK_5_1)
				.writeUnknown(Bit.UNK_5_2);

		for (ShipSystem sys : ShipSystem.values()) {
			Bit bit = SYSTEM_DAMAGES[sys.ordinal()];
			writer.writeFloat(bit, npc.getSystemDamage(sys), -1);
		}

		for (BeamFrequency freq : BeamFrequency.values()) {
			Bit bit = SHLD_FREQS[freq.ordinal()];
			writer.writeFloat(bit, npc.getShieldFreq(freq), -1);
		}
	}
}
