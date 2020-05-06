package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisNpc;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * ObjectParser implementation for NPC ships
 * @author rjwut
 */
public class NpcShipParser extends AbstractObjectParser {
	private enum Bit {
		NAME,
		IMPULSE,
		RUDDER,
		MAX_IMPULSE,
		MAX_TURN_RATE,
		UNK_1_6,
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

		SINGLE_SCAN,
		DOUBLE_SCAN,
		VISIBILITY,
		SIDE,
		UNK_4_5,
		UNK_4_6,
		UNK_4_7,
		TARGET_X,

		TARGET_Y,
		TARGET_Z,
		TAGGED,
		UNK_5_4,
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
        obj.setImpulse(reader.readFloat(Bit.IMPULSE));
        obj.setSteering(reader.readFloat(Bit.RUDDER));
        obj.setTopSpeed(reader.readFloat(Bit.MAX_IMPULSE));
        obj.setTurnRate(reader.readFloat(Bit.MAX_TURN_RATE));

        reader.readObjectUnknown(Bit.UNK_1_6, 4);

        obj.setHullId(reader.readInt(Bit.SHIP_TYPE, -1));
        obj.setX(reader.readFloat(Bit.X));
        obj.setY(reader.readFloat(Bit.Y));
        obj.setZ(reader.readFloat(Bit.Z));
        obj.setPitch(reader.readFloat(Bit.PITCH));
        obj.setRoll(reader.readFloat(Bit.ROLL));
        obj.setHeading(reader.readFloat(Bit.HEADING));
        obj.setVelocity(reader.readFloat(Bit.VELOCITY));
        obj.setSurrendered(reader.readBool(Bit.SURRENDERED, 1));

        reader.readObjectUnknown(Bit.UNK_2_8, 1);

        obj.setShieldsFront(reader.readFloat(Bit.FORE_SHIELD));
        obj.setShieldsFrontMax(reader.readFloat(Bit.FORE_SHIELD_MAX));
        obj.setShieldsRear(reader.readFloat(Bit.AFT_SHIELD));
        obj.setShieldsRearMax(reader.readFloat(Bit.AFT_SHIELD_MAX));

        reader.readObjectUnknown(Bit.UNK_3_5, 2);

        obj.setFleetNumber(reader.readByte(Bit.FLEET_NUMBER, Byte.MIN_VALUE));
        obj.setSpecialBits(reader.readInt(Bit.SPECIAL_ABILITIES, -1));
        obj.setSpecialStateBits(reader.readInt(Bit.SPECIAL_STATE, -1));

        if (reader.has(Bit.SINGLE_SCAN)) {
            obj.setScanLevelBits(1, reader.readInt(Bit.SINGLE_SCAN, 0));
        }

        if (reader.has(Bit.DOUBLE_SCAN)) {
            obj.setScanLevelBits(2, reader.readInt(Bit.DOUBLE_SCAN, 0));
        }

        if (reader.has(Bit.VISIBILITY)) {
            obj.setVisibilityBits(reader.readInt(Bit.VISIBILITY, 0));
        }

        obj.setSide(reader.readByte(Bit.SIDE, (byte) -1));

        reader.readObjectUnknown(Bit.UNK_4_5, 1);
        reader.readObjectUnknown(Bit.UNK_4_6, 1);
        reader.readObjectUnknown(Bit.UNK_4_7, 1);

        obj.setTargetX(reader.readFloat(Bit.TARGET_X));
        obj.setTargetY(reader.readFloat(Bit.TARGET_Y));
        obj.setTargetZ(reader.readFloat(Bit.TARGET_Z));
        obj.setNpcTagged(reader.readBool(Bit.TAGGED, 1));

        reader.readObjectUnknown(Bit.UNK_5_4, 1);

        // system damage
        for (ShipSystem sys : ShipSystem.values()) {
        	obj.setSystemDamage(sys, reader.readFloat(SYSTEM_DAMAGES[sys.ordinal()]));
        }

        // shield frequencies
        for (BeamFrequency freq : BeamFrequency.values()) {
        	obj.setShieldFreq(freq, reader.readFloat(SHLD_FREQS[freq.ordinal()]));
        }

        return obj;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisNpc npc = (ArtemisNpc) obj;
		writer	.writeString(Bit.NAME, npc.getName())
				.writeFloat(Bit.IMPULSE, npc.getImpulse())
				.writeFloat(Bit.RUDDER, npc.getSteering())
				.writeFloat(Bit.MAX_IMPULSE, npc.getTopSpeed())
				.writeFloat(Bit.MAX_TURN_RATE, npc.getTurnRate())
				.writeUnknown(Bit.UNK_1_6)
				.writeInt(Bit.SHIP_TYPE, npc.getHullId(), -1)
				.writeFloat(Bit.X, npc.getX())
				.writeFloat(Bit.Y, npc.getY())
				.writeFloat(Bit.Z, npc.getZ())
				.writeFloat(Bit.PITCH, npc.getPitch())
				.writeFloat(Bit.ROLL, npc.getRoll())
				.writeFloat(Bit.HEADING, npc.getHeading())
				.writeFloat(Bit.VELOCITY, npc.getVelocity())
				.writeBool(Bit.SURRENDERED, npc.isSurrendered(), 1)
				.writeUnknown(Bit.UNK_2_8)
				.writeFloat(Bit.FORE_SHIELD, npc.getShieldsFront())
				.writeFloat(Bit.FORE_SHIELD_MAX, npc.getShieldsFrontMax())
				.writeFloat(Bit.AFT_SHIELD, npc.getShieldsRear())
				.writeFloat(Bit.AFT_SHIELD_MAX, npc.getShieldsRearMax())
				.writeUnknown(Bit.UNK_3_5)
				.writeByte(Bit.FLEET_NUMBER, npc.getFleetNumber(), Byte.MIN_VALUE)
				.writeInt(Bit.SPECIAL_ABILITIES, npc.getSpecialBits(), -1)
				.writeInt(Bit.SPECIAL_STATE, npc.getSpecialStateBits(), -1);

		Integer scanLevel = npc.getScanLevelBits(1);

		if (scanLevel != null) {
			writer.writeInt(Bit.SINGLE_SCAN, scanLevel, 1);
		}

		scanLevel = npc.getScanLevelBits(2);

		if (scanLevel != null) {
			writer.writeInt(Bit.DOUBLE_SCAN, scanLevel, 1);
		}

		Integer visibility = npc.getVisibilityBits();

		if (visibility != null) {
			writer.writeInt(Bit.VISIBILITY, visibility, 1);
		}

		writer	.writeByte(Bit.SIDE, npc.getSide(), (byte) -1)
				.writeUnknown(Bit.UNK_4_5)
				.writeUnknown(Bit.UNK_4_6)
				.writeUnknown(Bit.UNK_4_7)
				.writeFloat(Bit.TARGET_X, npc.getTargetX())
				.writeFloat(Bit.TARGET_Y, npc.getTargetY())
				.writeFloat(Bit.TARGET_Z, npc.getTargetZ())
				.writeBool(Bit.TAGGED, npc.isNpcTagged(), 1)
				.writeUnknown(Bit.UNK_5_4);

		for (ShipSystem sys : ShipSystem.values()) {
			writer.writeFloat(SYSTEM_DAMAGES[sys.ordinal()], npc.getSystemDamage(sys));
		}

		for (BeamFrequency freq : BeamFrequency.values()) {
			writer.writeFloat(SHLD_FREQS[freq.ordinal()], npc.getShieldFreq(freq));
		}
	}
}
