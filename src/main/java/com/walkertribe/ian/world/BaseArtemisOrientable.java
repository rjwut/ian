package com.walkertribe.ian.world;

import java.util.SortedMap;

/**
 * Base implementation for ArtemisOrientable objects.
 * @author rjwut
 */
public abstract class BaseArtemisOrientable extends BaseArtemisObject
		implements ArtemisOrientable {
    public BaseArtemisOrientable(int objId) {
		super(objId);
	}

	private float mHeading = Float.NaN;
    private float mPitch = Float.NaN;
    private float mRoll = Float.NaN;

    @Override
	public float getHeading() {
    	return mHeading;
	}

	@Override
	public void setHeading(float heading) {
		mHeading = heading;
	}

	@Override
	public float getPitch() {
		return mPitch;
	}

	@Override
	public void setPitch(float pitch) {
		mPitch = pitch;
	}

	@Override
	public float getRoll() {
		return mRoll;
	}

	@Override
	public void setRoll(float roll) {
		mRoll = roll;
	}

	@Override
    public void updateFrom(ArtemisObject obj) {
		super.updateFrom(obj);

		if (obj instanceof ArtemisOrientable) {
			ArtemisOrientable cast = (ArtemisOrientable) obj;
			float heading = cast.getHeading();
	        float pitch = cast.getPitch();
	        float roll = cast.getRoll();

	        if (!Float.isNaN(heading)) {
	        	mHeading = heading;
	        }

	        if (!Float.isNaN(pitch)) {
	        	mPitch = pitch;
	        }

	        if (!Float.isNaN(roll)) {
	        	mRoll = roll;
	        }
		}
    }

	@Override
	protected void appendObjectProps(SortedMap<String, Object> props) {
		super.appendObjectProps(props);
    	putProp(props, "Heading", mHeading);
    	putProp(props, "Pitch", mPitch);
    	putProp(props, "Roll", mRoll);
    }

	/**
	 * Returns true if this object contains any data.
	 */
    protected boolean hasData() {
    	return super.hasData() || !Float.isNaN(mHeading) || !Float.isNaN(mPitch) || !Float.isNaN(mRoll);
    }
}