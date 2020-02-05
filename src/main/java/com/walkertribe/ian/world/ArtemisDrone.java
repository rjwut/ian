package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.ObjectType;

/**
 * Torgoth drones
 * @author rjwut
 */
public class ArtemisDrone extends BaseArtemisOrientable {
	private float mSteering = Float.NaN;
	private int mSide = -1;

	public ArtemisDrone(int objId) {
		super(objId);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.DRONE;
	}

	/**
	 * Current rudder position for the drone, as a value between 0 (hard port)
	 * and 1 (hard starboard).
	 * Unspecified: Float.NaN
	 */
	public float getSteering() {
		return mSteering;
	}

	public void setSteering(float steering) {
		this.mSteering = steering;
	}

	/**
	 * The side this drone belongs to.
	 * Unspecified: -1
	 */
	public int getSide() {
		return mSide;
	}

	public void setSide(int side) {
		mSide = side;
	}

	@Override
	public void updateFrom(ArtemisObject other) {
		super.updateFrom(other);

		if (other instanceof ArtemisDrone) {
			ArtemisDrone drone = (ArtemisDrone) other;

			if (!Float.isNaN(drone.mSteering)) {
				mSteering = drone.mSteering;
			}

			if (drone.mSide != -1) {
				mSide = drone.mSide;
			}
		}
	}

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "Rudder", mSteering);
    	putProp(props, "Side", mSide);
    }
}