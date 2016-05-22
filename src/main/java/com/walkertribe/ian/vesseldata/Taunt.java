package com.walkertribe.ian.vesseldata;

/**
 * Faction-specific taunts.
 * @author rjwut
 */
public class Taunt {
	private String immunity;
	private String text;

	Taunt(String immunity, String text) {
		this.immunity = immunity;
		this.text = text;
	}

	/**
	 * The indicator in the captain's psychological profile that indicates that
	 * they are immune to this taunt.
	 */
	public String getImmunity() {
		return immunity;
	}

	/**
	 * The actual text of the taunt.
	 */
	public String getText() {
		return text;
	}
}