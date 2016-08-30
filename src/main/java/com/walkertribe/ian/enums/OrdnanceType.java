package com.walkertribe.ian.enums;

/**
 * The types of ordnance that player ships can fire.
 * @author rjwut
 */
public enum OrdnanceType {
	HOMING("Homing"),
	NUKE("Nuke"),
	MINE("Mine"),
	EMP("EMP"),
	PSHOCK("PlasmaShock");

	public static final int COUNT = values().length;

	private String label;

	OrdnanceType(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}