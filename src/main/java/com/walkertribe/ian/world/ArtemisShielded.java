package com.walkertribe.ian.world;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * An ArtemisObject which can have shields. Note that shield values can be
 * negative.
 * @author dhleong
 */
public interface ArtemisShielded extends ArtemisOrientable {
    /**
     * Identifies the type of ship this is. This corresponds to the uniqueID
     * attribute of vessel elements in vesselData.xml.
     * Unspecified: -1
     */
    public int getHullId();
    public void setHullId(int value);

    /**
     * Returns the Vessel object corresponding to this object's hull ID in the
     * given VesselData object. If the hull ID is unspecified or vesselData.xml
     * contains no Vessel with that ID, getVessel() returns null.
     */
    public Vessel getVessel(Context ctx);

    /**
	 * The strength of the forward shields.
	 * Unspecified: Float.NaN
	 */
    public float getShieldsFront();
    public void setShieldsFront(float value);

    /**
	 * The strength of the aft shields.
	 * Unspecified: Float.NaN
	 */
    public float getShieldsRear();
    public void setShieldsRear(float value);

    /**
     * The maximum strength of the forward shields.
     * Unspecified: Float.NaN
     */
    public float getShieldsFrontMax();
    public void setShieldsFrontMax(float value);

    /**
     * The maximum strength of the aft shields.
     * Unspecified: Float.NaN
     */
    public float getShieldsRearMax();
    public void setShieldsRearMax(float value);

    /**
     * The side the ship is on. Ships on the same side are friendly to one another.
     * Unspecified: -1
     */
    public byte getSide();
    public void setSide(byte side);
}