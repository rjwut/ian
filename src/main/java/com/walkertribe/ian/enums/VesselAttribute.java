package com.walkertribe.ian.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * A list of attributes for Vessels. Corresponds to the &lt;vessel
 * broadType=""&gt; attribute in vesselData.xml.
 * @author rjwut
 */
public enum VesselAttribute {
	// vessel class
	BASE,			// immovable, allies can dock and resupply
	CARRIER,		// can launch fighters
	FIGHTER,		// launched from carriers
	LARGE,			// large non-player vessel
	MEDIUM,			// medium non-player vessel
	PLAYER,			// player can control
	SMALL,			// small non-player vessel

	// friendly type
	CARGO,			// unarmed
	LUXURY,			// unarmed
	SCIENCE,		// unarmed
	TRANSPORT,		// unarmed
	WARSHIP,		// armed, can assist players in combat

	// biomech traits
	ANOMALYEATER,	// eats anomalies
	ASTEROIDEATER,	// eats asteroids
	SENTIENT;		// can respond to hails and calm the tribe

	/**
	 * Returns a Set containing the VesselAttributes that correspond to the
	 * space-delimited list of attribute names in the given String.
	 */
	public static Set<VesselAttribute> build(String broadType) {
		String[] tokens = broadType.split(" ");
		Set<VesselAttribute> attrs = new HashSet<VesselAttribute>();

		for (String token : tokens) {
			attrs.add(VesselAttribute.valueOf(token.toUpperCase()));
		}

		return attrs;
	}
}