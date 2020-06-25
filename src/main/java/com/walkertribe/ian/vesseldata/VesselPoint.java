package com.walkertribe.ian.vesseldata;

/**
 * A location on a Vessel's 3D mesh.
 * @author rjwut
 */
public class VesselPoint {
	float x;
	float y;
	float z;

	/**
	 * Returns the point's X coordinate.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the point's Y coordinate.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the point's Z coordinate.
	 */
	public float getZ() {
		return z;
	}

	@Override
	public String toString() {
	    return "[" + x + "," + y + "," + z + "]";
	}
}