package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * Base implementation for ships (player or NPC).
 */
public abstract class BaseArtemisShip extends BaseArtemisShielded {
    private float mVelocity = Float.NaN;
    private final float[] mShieldFreqs = new float[5];
    private float mSteering = Float.NaN;
    private float mTopSpeed = Float.NaN;
    private float mTurnRate = Float.NaN;
    private float mImpulse = Float.NaN;
    private Integer mVisibility;

    public BaseArtemisShip(int objId) {
        super(objId);

        for (int i = 0; i < 5; i++) {
        	mShieldFreqs[i] = Float.NaN;
        }
    }

    public BaseArtemisShip(int objId, Vessel vessel) {
        this(objId);

        if (vessel != null) {
            setVessel(vessel);
            setArtemisClass(vessel.getName());
            setRace(vessel.getFaction().getName());
            setShieldsFront(vessel.getForeShields());
            setShieldsFrontMax(vessel.getForeShields());
            setShieldsRear(vessel.getAftShields());
            setShieldsRearMax(vessel.getAftShields());
            setTopSpeed(vessel.getTopSpeed());
            setTurnRate(vessel.getTurnRate());
        }
    }

    /**
     * Current speed of the ship: 0.0 = all stop, 1.0 = full speed
     * Unspecified: Float.NaN
     */
    public float getVelocity() {
        return mVelocity;
    }
    
    public void setVelocity(float velocity) {
        mVelocity = velocity;
    }

    /**
     * Rudder position for this ship: 0.0 = hard to port, 0.5 = rudder
     * amidships, 1.0 hard to starboard
     * Unspecified: Float.NaN
     */
    public float getSteering() {
        return mSteering;
    }

    public void setSteering(float steeringSlider) {
        mSteering = steeringSlider;
    }

    /**
     * The maximum speed of this ship, in ls (whatever that is).
     * Unspecified: -1
     */
    public float getTopSpeed() {
        return mTopSpeed;
    }

    public void setTopSpeed(float topSpeed) {
        mTopSpeed = topSpeed;
    }
    
    /**
     * The maximum turn rate of this ship.
     * Unspecified: Float.NaN
     */
    public float getTurnRate() {
        return mTurnRate;
    }
    
    public void setTurnRate(float turnRate) {
        mTurnRate = turnRate;
    }

    /**
     * A value between 0 and 1 indicating the shields' resistance to the given
     * BeamFrequency. Higher values indicate that the shields are more resistant
     * to that frequency.
     * Unspecified: Float.NaN
     */
    public float getShieldFreq(BeamFrequency freq) {
        return mShieldFreqs[freq.ordinal()];
    }
    
    public void setShieldFreq(BeamFrequency freq, float value) {
        mShieldFreqs[freq.ordinal()] = value;
    }

    /**
     * Impulse setting, as a value from 0 (all stop) and 1 (full impulse).
     * Unspecified: Float.NaN
     */
    public float getImpulse() {
        return mImpulse;
    }

    public void setImpulse(float impulseSlider) {
        mImpulse = impulseSlider;
    }

    /**
     * Returns whether this ship is visible to the given side on map screens.
     * Unspecified: UNKNOWN
     */
    @Override
    public BoolState getVisibility(int side) {
        if (getSide() == side && side != -1) {
            return BoolState.TRUE; // ships are always visible to their own side
        }

        return mVisibility == null ? BoolState.UNKNOWN : BoolState.from((mVisibility & (1 << side)) == 1);
    }

    /**
     * Sets the visibility of this ship for the indicated side.
     */
    public void setVisibility(int side, boolean visible) {
    	if (mVisibility == null) {
    		mVisibility = 0;
    	}

    	mVisibility |= 1 << side;
    }

    /**
     * Returns the raw bits for visibility.
     */
    public Integer getVisibilityBits() {
    	return mVisibility;
    }

    /**
     * Sets the raw bits for visibility.
     */
    public void setVisibilityBits(int bits) {
    	mVisibility = bits;
    }

    @Override
    public void updateFrom(ArtemisObject obj) {
        super.updateFrom(obj);
        
        if (obj instanceof BaseArtemisShip) {
            BaseArtemisShip ship = (BaseArtemisShip) obj;
            
            if (!Float.isNaN(ship.mSteering)) {
                mSteering = ship.mSteering;
            }
            
            if (!Float.isNaN(ship.mVelocity)) {
                mVelocity = ship.mVelocity;
            }

            if (!Float.isNaN(ship.mTopSpeed)) {
                mTopSpeed = ship.mTopSpeed;
            }

            if (!Float.isNaN(ship.mTurnRate)) {
                mTurnRate = ship.mTurnRate;
            }

            if (!Float.isNaN(ship.mImpulse)) {
            	mImpulse = ship.mImpulse;
            }

            for (int i = 0; i < mShieldFreqs.length; i++) {
            	float value = ship.mShieldFreqs[i];

            	if (!Float.isNaN(value)) {
                    mShieldFreqs[i] = value;
            	}
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "Velocity", mVelocity);
    	BeamFrequency[] freqs = BeamFrequency.values();

    	for (int i = 0; i < mShieldFreqs.length; i++) {
    		putProp(props, "Shield frequency " + freqs[i], mShieldFreqs[i]);
    	}

    	putProp(props, "Rudder", mSteering);
    	putProp(props, "Top speed", mTopSpeed);
    	putProp(props, "Turn rate", mTurnRate);
    	putProp(props, "Impulse", mImpulse);
    }

    /**
     * Returns true if this object contains any data.
     */
    protected boolean hasData() {
    	if (super.hasData()) {
    		return true;
    	}

    	if (
    			!Float.isNaN(mVelocity) ||
    			!Float.isNaN(mSteering) ||
    			!Float.isNaN(mTopSpeed) ||
    			!Float.isNaN(mTurnRate) ||
    			!Float.isNaN(mImpulse)
    	) {
    		return true;
    	}

    	for (float freq : mShieldFreqs) {
    		if (!Float.isNaN(freq)) {
    			return true;
    		}
    	}

    	return false;
    }
}