package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.OrdnanceType;

/**
 * A torpedo in flight.
 */
public class ArtemisTorpedo extends BaseArtemisObject {
	private float mDx = Float.NaN;
	private float mDy = Float.NaN;
	private float mDz = Float.NaN;
	private OrdnanceType mOrdnanceType;

	public ArtemisTorpedo(int objId) {
		super(objId);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.TORPEDO;
	}

	/**
	 * Delta X
	 * Unspecified: Float.NaN
	 */
	public float getDx() {
		return mDx;
	}

	public void setDx(float dx) {
		mDx = dx;
	}

	/**
	 * Delta Y
	 * Unspecified: Float.NaN
	 */
	public float getDy() {
		return mDy;
	}

	public void setDy(float dy) {
		mDy = dy;
	}

	/**
	 * Delta Z
	 * Unspecified: Float.NaN
	 */
	public float getDz() {
		return mDz;
	}

	public void setDz(float dz) {
		mDz = dz;
	}

	/**
	 * Ordnance type
	 * Unspecified: null
	 */
	public OrdnanceType getOrdnanceType() {
		return mOrdnanceType;
	}

	public void setOrdnanceType(OrdnanceType ordnanceType) {
		mOrdnanceType = ordnanceType;
	}

    @Override
    public void updateFrom(ArtemisObject other) {
        super.updateFrom(other);

        if (other instanceof ArtemisTorpedo) {
            ArtemisTorpedo t = (ArtemisTorpedo) other;

            if (!Float.isNaN(t.mDx)) {
            	mDx = t.mDx;
            }

            if (!Float.isNaN(t.mDy)) {
            	mDy = t.mDy;
            }

            if (!Float.isNaN(t.mDz)) {
            	mDz = t.mDz;
            }

            if (t.mOrdnanceType != null) {
            	mOrdnanceType = t.mOrdnanceType;
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "dx", mDx);
    	putProp(props, "dy", mDy);
    	putProp(props, "dz", mDz);
    	putProp(props, "Ordnance type", mOrdnanceType);
    }
}
