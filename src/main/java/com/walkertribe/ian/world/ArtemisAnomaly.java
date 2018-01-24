package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.Upgrade;

/**
 * An anomaly (objects which can be picked up for upgrades)
 * @author rjwut
 */
public class ArtemisAnomaly extends BaseArtemisObject {
	private Upgrade mUpgrade;

	public ArtemisAnomaly(int objId) {
		super(objId);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.ANOMALY;
	}

	/**
	 * Returns the Upgrade you receive for picking up this anomaly.
	 */
	public Upgrade getUpgrade() {
		return mUpgrade;
	}

	public void setUpgrade(Upgrade upgrade) {
		mUpgrade = upgrade;
	}

    @Override
    public void updateFrom(ArtemisObject obj, Context ctx) {
        super.updateFrom(obj, ctx);
        
        if (obj instanceof ArtemisAnomaly) {
        	ArtemisAnomaly anomaly = (ArtemisAnomaly) obj;

            if (anomaly.mUpgrade != null) {
            	mUpgrade = anomaly.mUpgrade;
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props, boolean includeUnspecified) {
    	super.appendObjectProps(props, includeUnspecified);
    	putProp(props, "Upgrade", mUpgrade, includeUnspecified);
    }
}