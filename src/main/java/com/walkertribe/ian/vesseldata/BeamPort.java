package com.walkertribe.ian.vesseldata;

/**
 * Describes a single beam port on a vessel. Corresponds to the &lt;beam_port&gt;
 * element in vesselData.xml. This class extends WeaponPort and adds an arcWidth
 * property to define the beam weapon's firing arc.
 * @author rjwut
 */
public class BeamPort extends WeaponPort {
	float arcWidth;

	/**
	 * Returns the width of the beam arc in radians.
	 */
	public float getArcWidth() {
		return arcWidth;
	}
}