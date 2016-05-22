package com.walkertribe.ian.enums;

/**
 * The eight ship systems.
 * @author rjwut
 */
public enum ShipSystem {
	BEAMS("Beams"),
	TORPEDOES("Torpedoes"),
	SENSORS("Sensors"),
	MANEUVERING("Maneuvering"),
	IMPULSE("Impulse"),
	WARP_JUMP_DRIVE("Warp/Jump Drive"),
	FORE_SHIELDS("Fore shields"),
	AFT_SHIELDS("Aft shields");

	private String label;

	ShipSystem(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}