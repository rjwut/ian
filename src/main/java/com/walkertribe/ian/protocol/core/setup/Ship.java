package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * Represents a ship in AllShipSettingsPacket and SetShipSettingsPacket.
 * @author rjwut
 */
public class Ship {
	private CharSequence mName;
	private int mShipType;
	private float mAccentColor;
	private DriveType mDrive;

	public Ship(CharSequence name, int shipType, float accentColor, DriveType drive) {
		setName(name);
		setShipType(shipType);
		setAccentColor(accentColor);
		setDrive(drive);
	}

	/**
	 * The name of the ship
	 */
	public CharSequence getName() {
		return mName;
	}

	public void setName(CharSequence name) {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException("You must provide a name");
		}

		mName = name;
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
		if (accentColor < 0.0f || accentColor >= 1.0f) {
			throw new IllegalArgumentException("Accent color must be in range [0.0,1.0)");
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
}
