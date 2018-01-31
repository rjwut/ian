package com.walkertribe.ian.vesseldata;

/**
 * Mutable extension of Faction for testing.
 */
public class MutableFaction extends Faction {
	public MutableFaction(int id, String name, String keys) {
		super(id, name, keys);
	}

	public void addTaunt(String immunity, String text) {
		taunts.add(new Taunt(immunity, text));
	}
}
