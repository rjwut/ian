package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.OrdnanceType;

/**
 * A torpedo in flight.
 */
public class ArtemisTorpedo extends BaseArtemisObject {
	private float mDx = Float.MIN_VALUE;
	private float mDy = Float.MIN_VALUE;
	private float mDz = Float.MIN_VALUE;
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
	 * Unspecified: Float.MIN_VALUE
	 */
	public float getDx() {
		return mDx;
	}

	public void setDx(float dx) {
		mDx = dx;
	}

	/**
	 * Delta Y
	 * Unspecified: Float.MIN_VALUE
	 */
	public float getDy() {
		return mDy;
	}

	public void setDy(float dy) {
		mDy = dy;
	}

	/**
	 * Delta Z
	 * Unspecified: Float.MIN_VALUE
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
    public void updateFrom(ArtemisObject other, Context ctx) {
        super.updateFrom(other, ctx);

        if (other instanceof ArtemisTorpedo) {
            ArtemisTorpedo t = (ArtemisTorpedo) other;

            if (t.mDx != Float.MIN_VALUE) {
            	mDx = t.mDx;
            }

            if (t.mDy != Float.MIN_VALUE) {
            	mDy = t.mDy;
            }

            if (t.mDz != Float.MIN_VALUE) {
            	mDz = t.mDz;
            }

            if (t.mOrdnanceType != null) {
            	mOrdnanceType = t.mOrdnanceType;
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props, boolean includeUnspecified) {
    	super.appendObjectProps(props, includeUnspecified);
    	putProp(props, "Δx", mDx, Float.MIN_VALUE, includeUnspecified);
    	putProp(props, "Δy", mDy, Float.MIN_VALUE, includeUnspecified);
    	putProp(props, "Δz", mDz, Float.MIN_VALUE, includeUnspecified);
    	putProp(props, "Ordnance type", mOrdnanceType, includeUnspecified);
    }
}
