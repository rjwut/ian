package com.walkertribe.ian.vesseldata;

import java.util.Set;

import com.walkertribe.ian.util.Util;

/**
 * Constants representing the known broadType values in vesselData.xml. Mods
 * may introduce other broadType values; Artemis doesn't care, but scripts can
 * use them to spawn vessels, for example.
 * @author rjwut
 */
public final class VesselAttribute {
	// vessel class
	public static final String BASE = "base";       // immovable, allies can dock and resupply
	public static final String CARRIER = "carrier"; // can launch fighters
	public static final String FIGHTER = "fighter"; // launched from carriers
	public static final String LARGE = "large";     // large non-player vessel
	public static final String MEDIUM = "meduim";   // medium non-player vessel
	public static final String PLAYER = "player";   // player can control
	public static final String SMALL = "small";     // small non-player vessel

	// friendly type
	public static final String CARGO = "cargo";         // unarmed
	public static final String LUXURY = "luxury";       // unarmed
	public static final String SCIENCE = "science";     // unarmed
	public static final String TRANSPORT = "transport"; // unarmed
	public static final String WARSHIP = "warship";     // armed, can assist players in combat

	// biomech traits
	public static final String ANOMALYEATER = "anomalyeater";   // eats anomalies
	public static final String ASTEROIDEATER = "asteroideater"; // eats asteroids
	public static final String SENTIENT = "sentient";           // can respond to hails and calm the tribe

	/**
	 * Converts a space-delimited list (as given in broadType) into a Set of Strings.
	 */
	public static Set<String> build(CharSequence broadType) {
		return Util.splitSpaceDelimited(broadType.toString().toLowerCase());
	}

	private VesselAttribute() {
		// prevent instantiation
	}
}
