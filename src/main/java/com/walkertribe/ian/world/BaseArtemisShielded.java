package com.walkertribe.ian.world;

import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * Base implementation of a shielded world object.
 */
public abstract class BaseArtemisShielded extends BaseArtemisOrientable
		implements ArtemisShielded {
    private static final Pattern CALLSIGN_PATTERN = Pattern.compile("[A-Z]+\\d+");

    /**
     * Given the name of a contact, returns its callsign (e.g. V14) if it has one. Otherwise,
     * returns the contact's full name.
     */
    public static String extractCallsign(CharSequence name) {
        if (name != null) {
            Matcher matcher = CALLSIGN_PATTERN.matcher(name.toString());
            return matcher.find() ? matcher.group() : name.toString();
        }

        return null;
    }

    private int mHullId = -1;
    private float mShieldsFront = Float.NaN;
    private float mShieldsRear = Float.NaN;
    private float mShieldsFrontMax = Float.NaN;
    private float mShieldsRearMax = Float.NaN;
    private byte mSide = -1;

    public BaseArtemisShielded(int objId) {
        super(objId);
    }

    @Override
    public int getHullId() {
        return mHullId;
    }

    @Override
    public Vessel getVessel(Context ctx) {
   		return mHullId != -1 && ctx != null ? ctx.getVesselData().getVessel(mHullId) : null;
    }

    @Override
    public float getScale(Context ctx) {
    	Vessel vessel = getVessel(ctx);
    	return vessel != null ? vessel.getScale() : super.getScale(ctx);
    }

    @Override
    public Model getModel(Context ctx) {
    	Vessel vessel = getVessel(ctx);
    	return vessel != null ? vessel.getModel() : null;
    }

    @Override
    public void setHullId(int hullId) {
        mHullId = hullId;
    }

    public void setVessel(Vessel vessel) {
    	mHullId = vessel.getId();
    }

    @Override
    public float getShieldsFront() {
        return mShieldsFront;
    }

    @Override
    public void setShieldsFront(float shieldsFront) {
        mShieldsFront = shieldsFront;
    }

    @Override
    public float getShieldsRear() {
        return mShieldsRear;
    }

    @Override
    public void setShieldsRear(float shieldsRear) {
        mShieldsRear = shieldsRear;
    }

    @Override
    public float getShieldsFrontMax() {
        return mShieldsFrontMax;
    }

    @Override
    public void setShieldsFrontMax(float shieldsFrontMax) {
        mShieldsFrontMax = shieldsFrontMax;
    }

    @Override
    public float getShieldsRearMax() {
        return mShieldsRearMax;
    }

    @Override
    public void setShieldsRearMax(float shieldsRearMax) {
        mShieldsRearMax = shieldsRearMax;
    }

    @Override
    public byte getSide() {
        return mSide;
    }

    @Override
    public void setSide(byte side) {
        mSide = side;
    }

    @Override
    public void updateFrom(ArtemisObject obj) {
        super.updateFrom(obj);
        
        if (obj instanceof BaseArtemisShielded) {
            ArtemisShielded ship = (ArtemisShielded) obj;
            int hullId = ship.getHullId();

            if (hullId != -1) {
                mHullId = hullId;
            }

            float shields = ship.getShieldsFront();

            if (!Float.isNaN(shields)) {
                mShieldsFront = shields;
            }

            shields = ship.getShieldsRear();

            if (!Float.isNaN(shields)) {
                mShieldsRear = shields;
            }

            shields = ship.getShieldsFrontMax();

            if (!Float.isNaN(shields)) {
                mShieldsFrontMax = shields;
            }

            shields = ship.getShieldsRearMax();

            if (!Float.isNaN(shields)) {
                mShieldsRearMax = shields;
            }

            byte side = ship.getSide();

            if (side != -1) {
                mSide = side;
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "Hull ID", mHullId, -1);
        putProp(props, "Shields: fore", mShieldsFront);
        putProp(props, "Shields: aft", mShieldsRear);
        putProp(props, "Max shields: fore", mShieldsFrontMax);
        putProp(props, "Max shields: aft", mShieldsRearMax);
        putProp(props, "Side", mSide, -1);
    }

    /**
     * Returns true if this object contains any data.
     */
    protected boolean hasData() {
    	return super.hasData() || mHullId != -1 || !Float.isNaN(mShieldsFront) ||
    	        !Float.isNaN(mShieldsRear) || !Float.isNaN(mShieldsFrontMax) ||
    	        !Float.isNaN(mShieldsRearMax) || mSide != -1;
    }
}