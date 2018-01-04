package com.walkertribe.ian.enums;

import java.util.LinkedHashSet;
import java.util.Set;

import com.walkertribe.ian.util.Util;

/**
 * A list of attributes for Factions. Corresponds to the &lt;hullRace
 * keys=""&gt; attribute in vesselData.xml.
 * @author rjwut
 */
public enum FactionAttribute {
	// stance
	PLAYER,			// contains player-controlled vessels
	FRIENDLY,		// contains allied vessels
	ENEMY,			// contains hostile vessels

	// fleet organization
	STANDARD,		// forms the core of fleets
	SUPPORT,		// accompanies STANDARD enemies
	LONER,			// flies alone outside the fleet

	// behavior
	BIOMECH,		// hive mind, consumes asteroids/anomalies
	HASSPECIALS,	// has special abilities
	JUMPMASTER,		// has reduced jump cooldown and the combat jump (PLAYER only)
	WHALEHATER,		// gets distracted with hunting whales
	WHALELOVER;		// attack anyone who shoots whales

	/**
	 * Returns a Set containing the FactionAttributes that correspond to the
	 * space-delimited list of attribute names in the given String.
	 */
	public static Set<FactionAttribute> build(String keys) {
		String[] tokens = keys.split(" ");
		Set<FactionAttribute> attrs = new LinkedHashSet<FactionAttribute>();

		for (String token : tokens) {
			attrs.add(FactionAttribute.valueOf(token.toUpperCase()));
		}

		if (!Util.containsAny(attrs,  PLAYER, FRIENDLY, ENEMY)) {
			throw new IllegalArgumentException("Must have at least one of PLAYER, FRIENDLY, or ENEMY");
		}

		if (attrs.contains(ENEMY) && !Util.containsAny(attrs, STANDARD, SUPPORT, LONER)) {
			throw new IllegalArgumentException("ENEMY must have at least one of STANDARD, SUPPORT, or LONER");
		}

		if (attrs.contains(WHALEHATER) && attrs.contains(WHALELOVER)) {
			throw new IllegalArgumentException("WHALEHATER and WHALELOVER are mutually exclusive");
		}

		if (attrs.contains(JUMPMASTER) && !attrs.contains(PLAYER)) {
			throw new IllegalArgumentException("JUMPMASTER must be a PLAYER");
		}

		return attrs;
	}
}