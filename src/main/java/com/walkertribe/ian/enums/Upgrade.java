package com.walkertribe.ian.enums;

import java.util.Arrays;

/**
 * Upgrade types
 * @author rjwut
 */
public enum Upgrade {
	HIDENS_POWER_CELL(null),                  // +500 energy (on pickup)
	VIGORANIUM_NODULE(null),                  // resurrects 1 DAMCON (on pickup)
	CETROCITE_CRYSTAL(Console.ENGINEERING),   // coolant buff
	LATERAL_ARRAY(Console.SCIENCE),           // scanning is instantaneous
	TAURON_FOCUSER(Console.WEAPONS),          // +10% beam damage and cooldown buff
	INFUSION_P_COIL(Console.HELM),            // +10% warp and impulse speed
	CARPACTION_COIL(Console.WEAPONS),         // +10% shield recharge rate
	SECRET_CODE_CASE(Console.COMMUNICATIONS); // force 1 enemy to accept surrender
	/*
	 * Unknown types:
	 * HYDROGEN_RAM
	 * POLYPHASIC_CAPACITORS
	 * COOLANT_RESERVES
	 * ECM_STARPULSE
	 * WARTIME_PRODUCTION
	 * PROTONIC_VERNIERS
	 * REGENERATIVE_PAU_GRIDS
	 * VETERAN_DAMCON_TEAMS
	 * TACHYON_SCANNERS
	 * GRIDSCAN_OVERLOAD
	 * OVERRIDE_AUTHORIZATION
	 * RESUPPLY_IMPERATIVES
	 * PATROL_GROUP
	 * FAST_SUPPLY
	 * VANGUARD_REFIT (x6)
	 */

	public static final int STORABLE_UPGRADE_COUNT;

	static {
		STORABLE_UPGRADE_COUNT = getStorableUpgrades().length;
	}

	/**
	 * Returns an array containing the Upgrade values that players can store on
	 * the ship and use later.
	 */
	public static Upgrade[] getStorableUpgrades() {
		return Arrays.copyOfRange(values(), 2, 8);
	}

	private Console activatedBy;

	private Upgrade(Console activatedBy) {
		this.activatedBy = activatedBy;
	}

	/**
	 * Returns the Console that can activate this Upgrade, or null if the
	 * Upgrade is used immediately when picked up.
	 */
	public Console getActivatedBy() {
		return activatedBy;
	}
}