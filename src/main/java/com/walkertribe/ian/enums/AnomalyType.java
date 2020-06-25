package com.walkertribe.ian.enums;

/**
 * The various types of anomalies. Most are upgrades, but some aren't.
 * @author rjwut
 */
public enum AnomalyType {
	HIDENS_POWER_CELL("HiDens Power Cell", Upgrade.HIDENS_POWER_CELL),
	VIGORANIUM_NODULE("Vigoranium Nodule", Upgrade.VIGORANIUM_NODULE),
	CETROCITE_HEATSINKS("Cetrocite Crystal", Upgrade.CETROCITE_HEATSINKS),
	LATERAL_ARRAY("Lateral Array", Upgrade.LATERAL_ARRAY),
	TAURON_FOCUSERS("Tauron Focusers", Upgrade.TAURON_FOCUSERS),
	INFUSION_P_COILS("Infusion P-Coils", Upgrade.INFUSION_P_COILS),
	CARPACTION_COILS("Carapaction Coils", Upgrade.CARPACTION_COILS),
	SECRET_CODE_CASE("Secret Code Case", Upgrade.SECRET_CODE_CASE),
	BEACON("Beacon"),
	SPACE_JUNK("Space Junk");

    private String name;
	private Upgrade mUpgrade;

	private AnomalyType(String name) {
	    this.name = name;
	}

	private AnomalyType(String name, Upgrade upgrade) {
	    this(name);
		mUpgrade = upgrade;
	}

	/**
	 * The Upgrade you get from picking up an anomaly of this type, or null if
	 * you don't get an Upgrade.
	 */
	public Upgrade getUpgrade() {
		return mUpgrade;
	}

	@Override
	public String toString() {
	    return name;
	}
}
