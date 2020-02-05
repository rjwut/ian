package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * Base implementation of a shielded world object.
 */
public abstract class BaseArtemisShielded extends BaseArtemisOrientable
		implements ArtemisShielded {
    private int mHullId = -1;
    private float mShieldsFront = Float.NaN;
    private float mShieldsRear = Float.NaN;

    public BaseArtemisShielded(int objId) {
        super(objId);
    }

    @Override
    public int getHullId() {
        return mHullId;
    }

    @Override
    public Vessel getVessel(Context ctx) {
   		return mHullId != -1 ? ctx.getVesselData().getVessel(mHullId) : null;
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
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "Hull ID", mHullId, -1);
    	putProp(props, "Shields: fore", mShieldsFront);
    	putProp(props, "Shields: aft", mShieldsRear);
    }

    /**
     * Returns true if this object contains any data.
     */
    protected boolean hasData() {
    	return super.hasData() || mHullId != -1 || !Float.isNaN(mShieldsFront) || !Float.isNaN(mShieldsRear);
    }
}