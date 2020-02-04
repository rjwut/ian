package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.AlertStatus;
import com.walkertribe.ian.enums.BeaconMode;
import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.enums.MainScreenView;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.TargetingMode;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * ObjectParser implementation for player ships
 * @author rjwut
 */
public class PlayerShipParser extends AbstractObjectParser {
	private enum Bit {
    	WEAPONS_TARGET,
    	IMPULSE,
    	RUDDER,
    	TOP_SPEED,
    	TURN_RATE,
    	TARGETING_MODE,
    	WARP,
    	ENERGY,

    	SHIELD_STATE,
    	UNK_2_2,
    	SHIP_TYPE,
    	X,
    	Y,
    	Z,
    	PITCH,
    	ROLL,

    	HEADING,
    	VELOCITY,
    	NEBULA_TYPE,
    	NAME,
    	FORE_SHIELDS,
    	FORE_SHIELDS_MAX,
    	AFT_SHIELDS,
    	AFT_SHIELDS_MAX,

    	DOCKING_BASE,
    	ALERT_STATUS,
    	UNK_4_3,
    	MAIN_SCREEN,
    	BEAM_FREQUENCY,
    	AVAILABLE_COOLANT_OR_MISSILES,
    	SCIENCE_TARGET,
    	CAPTAIN_TARGET,

    	DRIVE_TYPE,
    	SCAN_OBJECT_ID,
    	SCAN_PROGRESS,
    	REVERSE_STATE,
    	CLIMB_DIVE,
    	SIDE,
    	VISIBILITY,
    	SHIP_INDEX,

    	CAPITAL_SHIP_ID,
    	ACCENT_COLOR,
    	EMERGENCY_JUMP_COOLDOWN,
    	BEACON_TYPE,
    	BEACON_MODE
    }
	private static final int BIT_COUNT = Bit.values().length;

    protected PlayerShipParser() {
		super(ObjectType.PLAYER_SHIP);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisPlayer parseImpl(PacketReader reader) {
        ArtemisPlayer player = new ArtemisPlayer(reader.getObjectId(), ObjectType.PLAYER_SHIP);
        player.setWeaponsTarget(reader.readInt(Bit.WEAPONS_TARGET, -1));
        player.setImpulse(reader.readFloat(Bit.IMPULSE));
        player.setSteering(reader.readFloat(Bit.RUDDER));
        player.setTopSpeed(reader.readFloat(Bit.TOP_SPEED));
        player.setTurnRate(reader.readFloat(Bit.TURN_RATE));

        if (reader.has(Bit.TARGETING_MODE)) {
            player.setTargetingMode(TargetingMode.values()[reader.readByte()]);
        }

        player.setWarp(reader.readByte(Bit.WARP, (byte) -1));
        player.setEnergy(reader.readFloat(Bit.ENERGY));

        if (reader.has(Bit.SHIELD_STATE)) {
        	player.setShields(reader.readBool(Bit.SHIELD_STATE, 2));
        }

        reader.readObjectUnknown(Bit.UNK_2_2, 4);
        player.setHullId(reader.readInt(Bit.SHIP_TYPE, -1));
        player.setX(reader.readFloat(Bit.X));
        player.setY(reader.readFloat(Bit.Y));
        player.setZ(reader.readFloat(Bit.Z));
        player.setPitch(reader.readFloat(Bit.PITCH));
        player.setRoll(reader.readFloat(Bit.ROLL));
        player.setHeading(reader.readFloat(Bit.HEADING));
        player.setVelocity(reader.readFloat(Bit.VELOCITY));
        player.setNebulaType(reader.readByte(Bit.NEBULA_TYPE, (byte) -1));
        player.setName(reader.readString(Bit.NAME));
        player.setShieldsFront(reader.readFloat(Bit.FORE_SHIELDS));
        player.setShieldsFrontMax(reader.readFloat(Bit.FORE_SHIELDS_MAX));
        player.setShieldsRear(reader.readFloat(Bit.AFT_SHIELDS));
        player.setShieldsRearMax(reader.readFloat(Bit.AFT_SHIELDS_MAX));
        player.setDockingBase(reader.readInt(Bit.DOCKING_BASE, -1));

        if (reader.has(Bit.ALERT_STATUS)) {
            player.setAlertStatus(AlertStatus.values()[reader.readByte()]);
        }

        reader.readObjectUnknown(Bit.UNK_4_3, 4);

        if (reader.has(Bit.MAIN_SCREEN)) {
            player.setMainScreen(MainScreenView.values()[reader.readByte()]);
        }

        if (reader.has(Bit.BEAM_FREQUENCY)) {
        	player.setBeamFrequency(BeamFrequency.values()[reader.readByte()]);
        }

        player.setAvailableCoolantOrMissiles(reader.readByte(Bit.AVAILABLE_COOLANT_OR_MISSILES, (byte) -1));
        player.setScienceTarget(reader.readInt(Bit.SCIENCE_TARGET, -1));
        player.setCaptainTarget(reader.readInt(Bit.CAPTAIN_TARGET, -1));

        if (reader.has(Bit.DRIVE_TYPE)) {
        	player.setDriveType(DriveType.values()[reader.readByte()]);
        }

        player.setScanObjectId(reader.readInt(Bit.SCAN_OBJECT_ID, -1));

        if (reader.has(Bit.SCAN_PROGRESS)) {
            player.setScanProgress(reader.readFloat());
        }

        player.setReverse(reader.readBool(Bit.REVERSE_STATE, 1));
        player.setClimbDive(reader.readFloat(Bit.CLIMB_DIVE));
        player.setSide(reader.readByte(Bit.SIDE, (byte) -1));

        if (reader.has(Bit.VISIBILITY)) {
            player.setVisibilityBits(reader.readInt(Bit.VISIBILITY, 0));
        }

        player.setShipIndex(reader.readByte(Bit.SHIP_INDEX, Byte.MIN_VALUE));

        player.setCapitalShipId(reader.readInt(Bit.CAPITAL_SHIP_ID, -1));
        player.setAccentColor(reader.readFloat(Bit.ACCENT_COLOR));
        player.setEmergencyJumpCooldown(reader.readFloat(Bit.EMERGENCY_JUMP_COOLDOWN));

        if (reader.has(Bit.BEACON_TYPE)) {
            player.setBeaconType(CreatureType.values()[reader.readByte()]);
        }

        if (reader.has(Bit.BEACON_MODE)) {
        	player.setBeaconMode(BeaconMode.values()[reader.readByte()]);
        }

        return player;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisPlayer player = (ArtemisPlayer) obj;
		writer	.writeInt(Bit.WEAPONS_TARGET, player.getWeaponsTarget(), -1)
				.writeFloat(Bit.IMPULSE, player.getImpulse())
				.writeFloat(Bit.RUDDER, player.getSteering())
				.writeFloat(Bit.TOP_SPEED, player.getTopSpeed())
				.writeFloat(Bit.TURN_RATE, player.getTurnRate());

		TargetingMode targetingMode = player.getTargetingMode();

		if (targetingMode != null) {
			writer.writeByte(Bit.TARGETING_MODE, (byte) targetingMode.ordinal(), (byte) -1);
		}

		writer	.writeByte(Bit.WARP, player.getWarp(), (byte) -1)
				.writeFloat(Bit.ENERGY, player.getEnergy())
				.writeBool(Bit.SHIELD_STATE, player.getShieldsState(), 2)
				.writeUnknown(Bit.UNK_2_2)
				.writeInt(Bit.SHIP_TYPE, player.getHullId(), -1)
				.writeFloat(Bit.X, player.getX())
				.writeFloat(Bit.Y, player.getY())
				.writeFloat(Bit.Z, player.getZ())
				.writeFloat(Bit.PITCH, player.getPitch())
				.writeFloat(Bit.ROLL, player.getRoll())
				.writeFloat(Bit.HEADING, player.getHeading())
				.writeFloat(Bit.VELOCITY, player.getVelocity())
				.writeByte(Bit.NEBULA_TYPE, player.getNebulaType(), (byte) -1)
				.writeString(Bit.NAME, player.getName())
				.writeFloat(Bit.FORE_SHIELDS, player.getShieldsFront())
				.writeFloat(Bit.FORE_SHIELDS_MAX, player.getShieldsFrontMax())
				.writeFloat(Bit.AFT_SHIELDS, player.getShieldsRear())
				.writeFloat(Bit.AFT_SHIELDS_MAX, player.getShieldsRearMax())
				.writeInt(Bit.DOCKING_BASE, player.getDockingBase(), -1);

		AlertStatus alertStatus = player.getAlertStatus();

		if (alertStatus != null) {
			writer.writeByte(Bit.ALERT_STATUS, (byte) alertStatus.ordinal(), (byte) -1);
		}

		writer.writeUnknown(Bit.UNK_4_3);

		MainScreenView screen = player.getMainScreen();

		if (screen != null) {
			writer.writeByte(Bit.MAIN_SCREEN, (byte) screen.ordinal(), (byte) -1);
		}

		BeamFrequency beamFreq = player.getBeamFrequency();

		if (beamFreq != null) {
			writer.writeByte(Bit.BEAM_FREQUENCY, (byte) beamFreq.ordinal(), (byte) -1);
		}

		writer	.writeByte(Bit.AVAILABLE_COOLANT_OR_MISSILES, player.getAvailableCoolantOrMissiles(), (byte) -1)
				.writeInt(Bit.SCIENCE_TARGET, player.getScienceTarget(), -1)
				.writeInt(Bit.CAPTAIN_TARGET, player.getCaptainTarget(), -1);

		DriveType drive = player.getDriveType();

		if (drive != null) {
			writer.writeByte(Bit.DRIVE_TYPE, (byte) drive.ordinal(), (byte) -1);
		}

		writer	.writeInt(Bit.SCAN_OBJECT_ID, player.getScanObjectId(), -1);
		
		if (player.hasScanProgress()) {
			writer.writeObjectFloat(Bit.SCAN_PROGRESS, player.getScanProgress());
		}

		writer	.writeBool(Bit.REVERSE_STATE, player.getReverseState(), 1)
				.writeFloat(Bit.CLIMB_DIVE, player.getClimbDive())
				.writeByte(Bit.SIDE, player.getSide(), (byte) -1);

		Integer visibility = player.getVisibilityBits();

		if (visibility != null) {
			writer.writeInt(Bit.VISIBILITY, visibility, 1);
		}

		writer	.writeByte(Bit.SHIP_INDEX, player.getShipIndex(), Byte.MIN_VALUE)
				.writeInt(Bit.CAPITAL_SHIP_ID, player.getCapitalShipId(), -1)
				.writeFloat(Bit.ACCENT_COLOR, player.getAccentColor())
				.writeFloat(Bit.EMERGENCY_JUMP_COOLDOWN, player.getEmergencyJumpCooldown());

		CreatureType beaconType = player.getBeaconType();

		if (beaconType != null) {
			writer.writeByte(Bit.BEACON_TYPE, (byte) beaconType.ordinal(), (byte) -1);
		}

		BeaconMode beaconMode = player.getBeaconMode();

		if (beaconMode != null) {
			writer.writeByte(Bit.BEACON_MODE, (byte) beaconMode.ordinal(), (byte) -1);
		}
	}
}