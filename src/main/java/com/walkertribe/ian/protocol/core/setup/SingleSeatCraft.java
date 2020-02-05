package com.walkertribe.ian.protocol.core.setup;

import java.util.Set;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.util.Util;
import com.walkertribe.ian.vesseldata.Vessel;
import com.walkertribe.ian.vesseldata.VesselAttribute;

/**
 * A single-seat craft, such as a shuttle or fighter.
 * @author rjwut
 */
public class SingleSeatCraft {
	/**
	 * Read a single-seat craft from the given PacketReader.
	 */
	public static SingleSeatCraft read(PacketReader reader) {
		int id = reader.readInt();

		if (id == 0) {
			return null;
		}

		return new SingleSeatCraft(
				id,
				reader.readInt(),
				reader.readString(),
				reader.readString(),
				reader.readString()
		);
	}

	private int mHullId;
	private int mBayIndex;
	private CharSequence mName;
	private CharSequence mRace;
	private Set<String> mVesselAttributes;

	public SingleSeatCraft(int hullId, int bayIndex, CharSequence name, CharSequence race, CharSequence broadTypes) {
		if (bayIndex < 0) {
			throw new IllegalArgumentException("Invalid bay index: " + bayIndex);
		}

		if (Util.isBlank(name)) {
			throw new IllegalArgumentException("You must provide a name");
		}

		if (Util.isBlank(race)) {
			throw new IllegalArgumentException("You must provide a name");
		}

		if (Util.isBlank(broadTypes)) {
			throw new IllegalArgumentException("You must provide a broadTypes list");
		}

		mHullId = hullId;
		mBayIndex = bayIndex;
		mName = name;
		mRace = race;
		mVesselAttributes = VesselAttribute.build(broadTypes);
	}

	/**
	 * The ID corresponding to the craft's Vessel in vesselData.xml.
	 */
	public int getHullId() {
		return mHullId;
	}

	/**
	 * The Vessel for this craft.
	 */
	public Vessel getVessel(Context ctx) {
		return ctx.getVesselData().getVessel(mHullId);
	}

	/**
	 * The index for the craft's bay.
	 */
	public int getBayIndex() {
		return mBayIndex;
	}

	/**
	 * The craft's name
	 */
	public CharSequence getName() {
		return mName;
	}

	/**
	 * The craft's race (TSN, Ximni, Pirate, etc.)
	 */
	public CharSequence getRace() {
		return mRace;
	}

	/**
	 * Returns a set containing the attributes of this vessel, corresponding to
	 * the broadType attribute in vesselData.xml.
	 */
	public Set<String> getVesselAttributes() {
		return mVesselAttributes;
	}

	/**
	 * Writes this craft to the given PacketWriter.
	 */
	public void write(PacketWriter writer) {
		writer.writeInt(mHullId);
		writer.writeInt(mBayIndex);
		writer.writeString(mName);
		writer.writeString(mRace);
		writer.writeString(Util.joinSpaceDelimited(mVesselAttributes));
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(mBayIndex)
				.append(": ")
				.append(mName)
				.append(" (hullId=")
				.append(mHullId)
				.append(", race=")
				.append(mRace)
				.append(", attrs=")
				.append(Util.joinSpaceDelimited(mVesselAttributes))
				.append(')')
				.toString();
	}
}
