package com.walkertribe.ian.enums;

import java.util.Arrays;

/**
 * Upgrade types
 * @author rjwut
 */
public enum Upgrade {
	HIDENS_POWER_CELL,                        // +500 energy (on pickup)
	VIGORANIUM_NODULE,                        // resurrects 1 DAMCON (on pickup)
	INFUSION_P_COILS(Console.HELM),           // +10% warp and impulse speed for 5 min
	HYDROGEN_RAM,                             // 10% turn speed boost for 5 min
	TAURON_FOCUSERS(Console.WEAPONS),         // +10% beam and reload speed for 5 min
	CARPACTION_COILS(Console.WEAPONS),        // +10% shield recharge rate for 5 min
	POLYPHASIC_CAPACITORS,                    // +10% energy recharge for 5 min
	COOLANT_RESERVES,                         // +10% heat reduction for 5 min
	LATERAL_ARRAY(Console.SCIENCE),           // 3x scan speed for 5 minutes
	ECM_STARPULSE,                            // enemy can't get target lock for 5 min
	SECRET_CODE_CASE(Console.COMMUNICATIONS), // force 1 enemy to accept surrender
	WARTIME_PRODUCTION,                       // 10% (min +1) boost to all starting base munitions
	INFUSION_P_COILS_PERM,                    // permanent +10% warp and impulse speed
	PROTONIC_VERNIERS,                        // permanent +10% turn speed
	TAURON_FOCUSERS_PERM,                     // permanent +10% beam and reload speed
	REGENERATIVE_PAU_GRIDS,                   // permanent +10% shield recharge rate
	VETERAN_DAMCON_TEAMS,                     // +10% DamCon move/repair speed
	CETROCITE_HEATSINKS(Console.ENGINEERING), // +10% reduction in heat generation
	TACHYON_SCANNERS,                         // permanent +10% scan speed
	GRIDSCAN_OVERLOAD,                        // permanent +10% sensor range boost
	OVERRIDE_AUTHORIZATION,                   // +10% base production speed
	RESUPPLY_IMPERATIVES,                     // +10% more missions
	PATROL_GROUP,                             // +1 TSN escort
	FAST_SUPPLY,                              // +1 TSN cargo ship
	VANGUARD_REFIT_HELM,                      // permanent +10% impulse/warp/turn speed
	VANGUARD_REFIT_WEAP,                      // permanent +10% beam/shield/reload speed
	VANGUARD_REFIT_SCI,                       // permanent +10% scan speed and sensor range
	VANGUARD_REFIT_COMM,                      // permanent +10% station production speed
	VANGUARD_REFIT_ENG,                       // permanent +10% efficiency all eng systems
	VANGUARD_REFIT_ALL;                       // permanent +10% boost to all ship systems

	private static final Upgrade[] ALL = values();

	private static final Upgrade[] ACTIVATION_UPGRADES = Arrays.copyOfRange(
			ALL,
			INFUSION_P_COILS.ordinal(),
			ALL.length
	);

	public static final int ACTIVATION_UPGRADE_COUNT = ACTIVATION_UPGRADES.length;

	static {
		for (int i = 0; i < ACTIVATION_UPGRADES.length; i++) {
			ACTIVATION_UPGRADES[i].activationIndex = i;
		}
	}

	/**
	 * Returns the Upgrade identified by the given index used in ArtemisPlayer
	 * objects and ActivateUpgradePackets.
	 */
	public static Upgrade fromIndex(int index) {
		if (index < 0 || index >= ACTIVATION_UPGRADES.length) {
			throw new IllegalArgumentException("Invalid activation index: " + index);
		}

		return ACTIVATION_UPGRADES[index];
	}

	/**
	 * Returns an array containing Upgrades that can be activated, ordered by
	 * activation index.
	 */
	public static Upgrade[] activation() {
		return Arrays.copyOf(ACTIVATION_UPGRADES, ACTIVATION_UPGRADES.length);
	}

	private Integer activationIndex;
	private Console activatedBy;

	private Upgrade() {
		// do nothing
	}

	private Upgrade(Console activatedBy) {
		this.activatedBy = activatedBy;
	}

	/**
	 * If this Upgrade can be stored and activated, returns the index used by
	 * ArtemisPlayer objects and ActivateUpgradePacket to refer to this Upgrade.
	 * Upgrades which can't be stored and activated return null.
	 */
	public Integer getActivationIndex() {
		return activationIndex;
	}

	/**
	 * If this Upgrade can be stored and activated, returns the Console that
	 * can activate the Upgrade. Upgrades which can't be stored and activated
	 * return null.
	 */
	public Console getActivatedBy() {
		return activatedBy;
	}
}