package com.walkertribe.ian.enums;

/**
 * The various types of anomalies. Most are upgrades, but some aren't.
 * @author rjwut
 */
public enum AnomalyType {
	HIDENS_POWER_CELL(Upgrade.HIDENS_POWER_CELL),
	VIGORANIUM_NODULE(Upgrade.VIGORANIUM_NODULE),
	CETROCITE_HEATSINKS(Upgrade.CETROCITE_HEATSINKS),
	LATERAL_ARRAY(Upgrade.LATERAL_ARRAY),
	TAURON_FOCUSERS(Upgrade.TAURON_FOCUSERS),
	INFUSION_P_COILS(Upgrade.INFUSION_P_COILS),
	CARPACTION_COILS(Upgrade.CARPACTION_COILS),
	SECRET_CODE_CASE(Upgrade.SECRET_CODE_CASE),
	BEACON,
	SPACE_JUNK;

	private Upgrade mUpgrade;

	private AnomalyType() {
		// for beacons and space junk
	}

	private AnomalyType(Upgrade upgrade) {
		mUpgrade = upgrade;
	}

	/**
	 * The Upgrade you get from picking up an anomaly of this type, or null if
	 * you don't get an Upgrade.
	 */
	public Upgrade getUpgrade() {
		return mUpgrade;
	}
}
