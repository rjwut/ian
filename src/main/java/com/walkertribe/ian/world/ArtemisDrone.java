package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.ObjectType;

public class ArtemisDrone extends BaseArtemisOrientable {
	private float mSteering = -1;

	public ArtemisDrone(int objId) {
		super(objId);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.DRONE;
	}

	public float getSteering() {
		return mSteering;
	}

	public void setSteering(float steering) {
		this.mSteering = steering;
	}

	@Override
	public void updateFrom(ArtemisObject other, Context ctx) {
		super.updateFrom(other, ctx);

		if (other instanceof ArtemisDrone) {
			ArtemisDrone drone = (ArtemisDrone) other;

			if (drone.mSteering != -1) {
				mSteering = drone.mSteering;
			}
		}
	}

    @Override
	public void appendObjectProps(SortedMap<String, Object> props, boolean includeUnspecified) {
    	super.appendObjectProps(props, includeUnspecified);
    	putProp(props, "Rudder", mSteering, -1, includeUnspecified);
    }
}