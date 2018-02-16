package com.walkertribe.ian.enums;

/**
 * The bridge consoles.
 * @author rjwut
 */
public enum Console {
	MAIN_SCREEN("Main screen"),
	HELM("Helm"),
	WEAPONS("Weapons"),
	ENGINEERING("Engineering"),
	SCIENCE("Science"),
	COMMUNICATIONS("Communications"),
	SINGLE_SEAT_CRAFT("Single-seat craft"),
	DATA("Data"),
	OBSERVER("Observer"),
	CAPTAINS_MAP("Captain's map"),
	GAME_MASTER("Game master");

	private String label;

	Console(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}