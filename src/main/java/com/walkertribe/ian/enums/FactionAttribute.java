package com.walkertribe.ian.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * A list of attributes for Factions. Corresponds to the <hullRace keys="">
 * attribute in vesselData.xml.
 * @author rjwut
 */
public enum FactionAttribute {
	// stance
	PLAYER,
	FRIENDLY,
	ENEMY,
	BIOMECH,

	// fleet behavior
	STANDARD,
	SUPPORT,
	LONER,

	// whale behavior
	WHALELOVER,
	WHALEHATER,

	// elite
	ELITE;

	/**
	 * Returns a Set containing the FactionAttributes that correspond to the
	 * space-delimited list of attribute names in the given String.
	 */
	public static Set<FactionAttribute> build(String keys) {
		String[] tokens = keys.split(" ");
		Set<FactionAttribute> attrs = new HashSet<FactionAttribute>();

		for (String token : tokens) {
			attrs.add(FactionAttribute.valueOf(token.toUpperCase()));
		}

		return attrs;
	}
}