package com.walkertribe.ian.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * Elite abilities that enemy ships may have.
 * @author rjwut
 */
public enum EliteAbility {
	STEALTH,      // invisible on long range or tactical views
	LOW_VIS,      // invisible on helm or weapons views until within 3k
	CLOAK,        // invisible on all views
	HET,          // high energy turn
	WARP,         // warp drive
	TELEPORT,     // jump drive
	TRACTOR,      // tractor beam
	DRONES,       // Torgoth drone launcher
	ANTI_MINE,    // can shoot mines
	ANTI_TORP,    // can shoot torpedoes
	SHIELD_DRAIN; // can drain your shields

	/**
	 * Returns a set containing the EliteAbility values that correspond to the
	 * given bit field value.
	 */
	public static Set<EliteAbility> fromValue(int value) {
		Set<EliteAbility> set = new HashSet<EliteAbility>();

		for (EliteAbility ability : values()) {
			if ((value & ability.bit) != 0) {
				set.add(ability);
			}
		}

		return set;
	}

	private int bit;

	EliteAbility() {
		bit = 0x01 << ordinal();
	}

	/**
	 * Given a bit field value, returns true if the bit corresponding to this
	 * EliteAbility is turned on; false otherwise.
	 */
	public boolean on(int value) {
		return (value & bit) != 0;
	}
}