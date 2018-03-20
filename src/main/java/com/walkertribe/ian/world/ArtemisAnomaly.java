package com.walkertribe.ian.world;

import java.util.SortedMap;

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
    public void updateFrom(ArtemisObject obj) {
        super.updateFrom(obj);
        
        if (obj instanceof ArtemisAnomaly) {
        	ArtemisAnomaly anomaly = (ArtemisAnomaly) obj;

            if (anomaly.mUpgrade != null) {
            	mUpgrade = anomaly.mUpgrade;
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "Upgrade", mUpgrade);
    }
}