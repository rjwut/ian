package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.AlertStatus;
import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.enums.MainScreenView;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.TargetingMode;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisPlayer;

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
    	SHIP_NUMBER,
    	SHIP_TYPE,
    	X,
    	Y,
    	Z,
    	PITCH,
    	ROLL,

    	HEADING,
    	VELOCITY,
    	UNK_3_3,
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
    	UNK_5_5,
    	UNK_5_6,
    	UNK_5_7,
    	UNK_5_8,

    	CAPITAL_SHIP_ID,
    	ACCENT_COLOR,
    	UNK_6_3
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
        player.setImpulse(reader.readFloat(Bit.IMPULSE, -1));
        player.setSteering(reader.readFloat(Bit.RUDDER, -1));
        player.setTopSpeed(reader.readFloat(Bit.TOP_SPEED, -1));
        player.setTurnRate(reader.readFloat(Bit.TURN_RATE, -1));

        if (reader.has(Bit.TARGETING_MODE)) {
            player.setTargetingMode(TargetingMode.values()[reader.readByte()]);
        }

        player.setWarp(reader.readByte(Bit.WARP, (byte) -1));
        player.setEnergy(reader.readFloat(Bit.ENERGY, -1));

        if (reader.has(Bit.SHIELD_STATE)) {
        	player.setShields(reader.readBool(Bit.SHIELD_STATE, 2).getBooleanValue());
        }

        player.setShipNumber(reader.readInt(Bit.SHIP_NUMBER, -1));
        player.setHullId(reader.readInt(Bit.SHIP_TYPE, -1));
        player.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        player.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        player.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));
        player.setPitch(reader.readFloat(Bit.PITCH, Float.MIN_VALUE));
        player.setRoll(reader.readFloat(Bit.ROLL, Float.MIN_VALUE));
        player.setHeading(reader.readFloat(Bit.HEADING, Float.MIN_VALUE));
        player.setVelocity(reader.readFloat(Bit.VELOCITY, -1));

        reader.readObjectUnknown(Bit.UNK_3_3, 2);

        player.setName(reader.readString(Bit.NAME));
        player.setShieldsFront(reader.readFloat(Bit.FORE_SHIELDS, Float.MIN_VALUE));
        player.setShieldsFrontMax(reader.readFloat(Bit.FORE_SHIELDS_MAX, -1));
        player.setShieldsRear(reader.readFloat(Bit.AFT_SHIELDS, Float.MIN_VALUE));
        player.setShieldsRearMax(reader.readFloat(Bit.AFT_SHIELDS_MAX, -1));
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
        player.setScanProgress(reader.readFloat(Bit.SCAN_PROGRESS, -1));
        player.setReverse(reader.readBool(Bit.REVERSE_STATE, 1));

        reader.readObjectUnknown(Bit.UNK_5_5, 4);
        reader.readObjectUnknown(Bit.UNK_5_6, 5);
        reader.readObjectUnknown(Bit.UNK_5_7, 4);
        reader.readObjectUnknown(Bit.UNK_5_8, 1);
        player.setCapitalShipId(reader.readInt(Bit.CAPITAL_SHIP_ID, -1));
        player.setAccentColor(reader.readFloat(Bit.ACCENT_COLOR, -1));
        reader.readObjectUnknown(Bit.UNK_6_3, 4);
        return player;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisPlayer player = (ArtemisPlayer) obj;
		int shipIndex = player.getShipNumber();
		int shipNumber = shipIndex == -1 ? -1 : shipIndex + 1;
		writer	.writeInt(Bit.WEAPONS_TARGET, player.getWeaponsTarget(), -1)
				.writeFloat(Bit.IMPULSE, player.getImpulse(), -1)
				.writeFloat(Bit.RUDDER, player.getSteering(), -1)
				.writeFloat(Bit.TOP_SPEED, player.getTopSpeed(), -1)
				.writeFloat(Bit.TURN_RATE, player.getTurnRate(), -1);

		TargetingMode targetingMode = player.getTargetingMode();

		if (targetingMode != null) {
			writer.writeByte(Bit.TARGETING_MODE, (byte) targetingMode.ordinal(), (byte) -1);
		}

		writer	.writeByte(Bit.WARP, player.getWarp(), (byte) -1)
				.writeFloat(Bit.ENERGY, player.getEnergy(), -1)
				.writeBool(Bit.SHIELD_STATE, player.getShieldsState(), 2)
				.writeInt(Bit.SHIP_NUMBER, shipNumber, -1)
				.writeInt(Bit.SHIP_TYPE, player.getHullId(), -1)
				.writeFloat(Bit.X, player.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, player.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, player.getZ(), Float.MIN_VALUE)
				.writeFloat(Bit.PITCH, player.getPitch(), Float.MIN_VALUE)
				.writeFloat(Bit.ROLL, player.getRoll(), Float.MIN_VALUE)
				.writeFloat(Bit.HEADING, player.getHeading(), Float.MIN_VALUE)
				.writeFloat(Bit.VELOCITY, player.getVelocity(), -1)
				.writeUnknown(Bit.UNK_3_3)
				.writeString(Bit.NAME, player.getName())
				.writeFloat(Bit.FORE_SHIELDS, player.getShieldsFront(), Float.MIN_VALUE)
				.writeFloat(Bit.FORE_SHIELDS_MAX, player.getShieldsFrontMax(), -1)
				.writeFloat(Bit.AFT_SHIELDS, player.getShieldsRear(), Float.MIN_VALUE)
				.writeFloat(Bit.AFT_SHIELDS_MAX, player.getShieldsRearMax(), -1)
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

		writer	.writeInt(Bit.SCAN_OBJECT_ID, player.getScanObjectId(), -1)
				.writeFloat(Bit.SCAN_PROGRESS, player.getScanProgress(), -1)
				.writeBool(Bit.REVERSE_STATE, player.getReverseState(), 1)
				.writeUnknown(Bit.UNK_5_5)
				.writeUnknown(Bit.UNK_5_6)
				.writeUnknown(Bit.UNK_5_7)
				.writeUnknown(Bit.UNK_5_8)
				.writeInt(Bit.CAPITAL_SHIP_ID, player.getCapitalShipId(), -1)
				.writeFloat(Bit.ACCENT_COLOR, player.getAccentColor(), -1)
				.writeUnknown(Bit.UNK_6_3);
	}
}