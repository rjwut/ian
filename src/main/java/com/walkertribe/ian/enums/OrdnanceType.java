package com.walkertribe.ian.enums;

/**
 * The types of ordnance that player ships can fire.
 * @author rjwut
 */
public enum OrdnanceType {
	HOMING("trp", "Homing"),
	NUKE("nuk", "Nuke"),
	MINE("min", "Mine"),
	EMP("emp", "EMP"),
	PSHOCK("shk", "PlasmaShock"),
	BEACON("bea", "Beacon"),
	PROBE("pro", "Probe"),
	TAG("tag", "Tag");

	public static final int COUNT = values().length;

	/**
	 * Returns the OrdnanceType corresponding to the given three-character
	 * code (as used in vesselData.xml) or null if no such OrdnanceType
	 * was found.
	 */
	public static OrdnanceType fromCode(String code) {
		for (OrdnanceType type : values()) {
			if (type.code.equals(code)) {
				return type;
			}
		}

		return null;
	}

	private String code;
	private String label;

	OrdnanceType(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return label;
	}
}