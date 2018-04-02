package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.BeamFrequency;

/**
 * Base implementation for ships (player or NPC).
 */
public abstract class BaseArtemisShip extends BaseArtemisShielded {
    private float mVelocity = -1;
    private float mShieldsFrontMax = Float.MIN_VALUE;
    private float mShieldsRearMax = Float.MIN_VALUE;
    private final float[] mShieldFreqs = new float[5];
    private float mSteering = -1;
    private float mTopSpeed = -1;
    private float mTurnRate = -1;
    private float mImpulse = -1;
    private byte mSide = -1;

    public BaseArtemisShip(int objId) {
        super(objId);

        for (int i = 0; i < 5; i++) {
        	mShieldFreqs[i] = -1;
        }
    }

    /**
     * Current speed of the ship: 0.0 = all stop, 1.0 = full speed
     * Unspecified: -1
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
     * Unspecified: -1
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
     * Unspecified: -1
     */
    public float getTurnRate() {
        return mTurnRate;
    }
    
    public void setTurnRate(float turnRate) {
        mTurnRate = turnRate;
    }

    /**
     * The maximum strength of the forward shield.
     * Unspecified: Float.MIN_VALUE
     */
    public float getShieldsFrontMax() {
        return mShieldsFrontMax;
    }

    public void setShieldsFrontMax(float shieldsFrontMax) {
        this.mShieldsFrontMax = shieldsFrontMax;
    }
    
    /**
     * The maximum strength of the aft shield.
     * Unspecified: Float.MIN_VALUE
     */
    public float getShieldsRearMax() {
        return mShieldsRearMax;
    }

    public void setShieldsRearMax(float shieldsRearMax) {
        this.mShieldsRearMax = shieldsRearMax;
    }

    /**
     * A value between 0 and 1 indicating the shields' resistance to the given
     * BeamFrequency. Higher values indicate that the shields are more resistant
     * to that frequency.
     * Unspecified: -1
     */
    public float getShieldFreq(BeamFrequency freq) {
        return mShieldFreqs[freq.ordinal()];
    }
    
    public void setShieldFreq(BeamFrequency freq, float value) {
        mShieldFreqs[freq.ordinal()] = value;
    }

    /**
     * Impulse setting, as a value from 0 (all stop) and 1 (full impulse).
     * Unspecified: -1
     */
    public float getImpulse() {
        return mImpulse;
    }

    public void setImpulse(float impulseSlider) {
        mImpulse = impulseSlider;
    }

    /**
     * The side this ship is on. There is no side 0. Biomechs are side 30.
     */
    public byte getSide() {
    	return mSide;
    }

    public void setSide(byte side) {
    	mSide = side;
    }

    @Override
    public void updateFrom(ArtemisObject obj) {
        super.updateFrom(obj);
        
        if (obj instanceof BaseArtemisShip) {
            BaseArtemisShip ship = (BaseArtemisShip) obj;
            
            if (ship.mSteering != -1) {
                mSteering = ship.mSteering;
            }
            
            if (ship.mVelocity != -1) {
                mVelocity = ship.mVelocity;
            }

            if (ship.mTopSpeed != -1) {
                mTopSpeed = ship.mTopSpeed;
            }

            if (ship.mTurnRate != -1) {
                mTurnRate = ship.mTurnRate;
            }
            
            if (ship.mShieldsFrontMax != Float.MIN_VALUE) {
                mShieldsFrontMax = ship.mShieldsFrontMax;
            }

            if (ship.mShieldsRearMax != Float.MIN_VALUE) {
                mShieldsRearMax = ship.mShieldsRearMax;
            }

            if (ship.mImpulse != -1) {
            	mImpulse = ship.mImpulse;
            }

            if (ship.mSide != -1) {
            	mSide = ship.mSide;
            }

            for (int i = 0; i < mShieldFreqs.length; i++) {
            	float value = ship.mShieldFreqs[i];

            	if (value != -1) {
                    mShieldFreqs[i] = value;
            	}
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "Velocity", mVelocity, -1);
    	putProp(props, "Shields: fore max", mShieldsFrontMax, Float.MIN_VALUE);
    	putProp(props, "Shields: aft max", mShieldsRearMax, Float.MIN_VALUE);
    	BeamFrequency[] freqs = BeamFrequency.values();

    	for (int i = 0; i < mShieldFreqs.length; i++) {
    		putProp(props, "Shield frequency " + freqs[i], mShieldFreqs[i], -1);
    	}

    	putProp(props, "Rudder", mSteering, -1);
    	putProp(props, "Top speed", mTopSpeed, -1);
    	putProp(props, "Turn rate", mTurnRate, -1);
    	putProp(props, "Impulse", mImpulse, -1);
    	putProp(props, "Side", mSide, -1);
    }

    /**
     * Returns true if this object contains any data.
     */
    protected boolean hasData() {
    	if (super.hasData()) {
    		return true;
    	}

    	if (
    			mVelocity != -1 ||
    			mShieldsFrontMax != Float.MIN_VALUE ||
    			mShieldsRearMax != Float.MIN_VALUE ||
    			mSteering != -1 ||
    			mTopSpeed != -1 ||
    			mTurnRate != -1 ||
    			mImpulse != -1 ||
    			mSide != -1
    	) {
    		return true;
    	}

    	for (float freq : mShieldFreqs) {
    		if (freq != -1) {
    			return true;
    		}
    	}

    	return false;
    }
}