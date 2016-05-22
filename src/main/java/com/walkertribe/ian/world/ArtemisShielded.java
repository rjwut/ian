package com.walkertribe.ian.world;

import com.walkertribe.ian.vesseldata.Vessel;

/**
 * An ArtemisObject which can have shields. Note that shield values can be
 * negative.
 * @author dhleong
 */
public interface ArtemisShielded extends ArtemisObject {
    /**
     * Identifies the type of ship this is. This corresponds to the uniqueID
     * attribute of vessel elements in vesselData.xml.
     * Unspecified: -1
     */
    public int getHullId();
    public void setHullId(int value);

    /**
     * Returns the Vessel object corresponding to this object's hull ID. If the
     * hull ID is unspecified or VesselData contains no Vessel with that ID,
     * getVessel() returns null.
     */
    public Vessel getVessel();

    /**
	 * The strength of the forward shields.
	 * Unspecified: Float.MIN_VALUE
	 */
    public float getShieldsFront();
    public void setShieldsFront(float value);

    /**
	 * The strength of the aft shields.
	 * Unspecified: Float.MIN_VALUE
	 */
    public float getShieldsRear();
    public void setShieldsRear(float value);
}