package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.Util;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * Represents a ship in AllShipSettingsPacket and SetShipSettingsPacket.
 * @author rjwut
 */
public class Ship {
	/**
	 * Reads a Ship from a PacketReader. Don't use the public Ship constructor in this scenario.
	 */
	public static Ship read(PacketReader reader) {
		DriveType drive = DriveType.values()[reader.readInt()];
		int hullId = reader.readInt();
		float color = reader.readFloat();
		BoolState hasName = reader.readBool(4);
		CharSequence name = null;

		if (hasName.getBooleanValue()) {
			name = reader.readString();
		}

		return new Ship(hasName, name, hullId, color, drive);
	}

	private BoolState mHasName;
	private CharSequence mName;
	private int mShipType;
	private float mAccentColor;
	private DriveType mDrive;

	/**
	 * Creates a new Ship.
	 */
	public Ship(CharSequence name, int shipType, float accentColor, DriveType drive) {
		this(shipType, accentColor, drive);
		setName(name);
	}

	/**
	 * Constructor for reading a Ship from a packet (which includes the hasName property).
	 */
	private Ship(BoolState hasName, CharSequence name, int shipType, float accentColor, DriveType drive) {
		this(shipType, accentColor, drive);
		mHasName = hasName;
		mName = name;
	}

	/**
	 * Common private constructor
	 */
	private Ship(int shipType, float accentColor, DriveType drive) {
		mShipType = shipType;
		setAccentColor(accentColor);
		setDrive(drive);
	}

	/**
	 * Returns BoolState.TRUE if this Ship has a name.
	 */
	public BoolState getHasName() {
		return mHasName;
	}

	/**
	 * The name of the ship
	 */
	public CharSequence getName() {
		return mName;
	}

	public void setName(CharSequence name) {
		mName = name;
		mHasName = BoolState.from(!Util.isBlank(name));
	}

	/**
	 * The hullId for this ship
	 */
	public int getShipType() {
		return mShipType;
	}

	public void setShipType(int shipType) {
		mShipType = shipType;
	}

	/**
	 * Returns the Vessel identified by the ship's hull ID, or null if no such Vessel can be found.
	 */
	public Vessel getVessel(Context ctx) {
		return ctx.getVesselData().getVessel(mShipType);
	}

	public void setVessel(Vessel vessel) {
		setShipType(vessel.getId());
	}

	/**
	 * The accent color for the ship
	 */
	public float getAccentColor() {
		return mAccentColor;
	}

	public void setAccentColor(float accentColor) {
		if (accentColor < 0.0f || accentColor > 1.0f) {
			throw new IllegalArgumentException("Accent color must be in range [0.0,1.0]");
		}

		mAccentColor = accentColor;
	}

	/**
	 * What drive type the ship is using
	 */
	public DriveType getDrive() {
		return mDrive;
	}

	public void setDrive(DriveType drive) {
		if (drive == null) {
			throw new IllegalArgumentException("You must provide a drive type");
		}

		mDrive = drive;
	}

	@Override
	public String toString() {
		return mName + ": (type #" + mShipType + ") [" + mDrive + "] color=" + (mAccentColor * 360) + " deg";
	}

	/**
	 * Writes this Ship to the given PacketWriter.
	 */
	public void write(PacketWriter writer) {
		writer	.writeInt(mDrive.ordinal())
				.writeInt(mShipType)
				.writeFloat(mAccentColor)
				.writeBytes(mHasName.toByteArray(4));

		if (mHasName.getBooleanValue()) {
			writer.writeString(mName);
		}
	}
}