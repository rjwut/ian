package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.Upgrade;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.TextUtil;

/**
 * An anomaly (objects which can be picked up for upgrades)
 * @author rjwut
 */
public class ArtemisAnomaly extends BaseArtemisObject {
	private Integer mScan;
	private Upgrade mUpgrade;

	public ArtemisAnomaly(int objId) {
		super(objId);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.ANOMALY;
	}

	/**
	 * Whether the given side has scanned this anomaly.
	 * Unspecified: UNKNOWN
	 */
	public BoolState isScanned(int side) {
		if (mScan == null) {
			return BoolState.UNKNOWN;
		}

		return BoolState.from((mScan & (1 << side)) != 0);
	}

	/**
	 * Sets whether the given side has scanned this anomaly.
	 */
	public void setScanned(int side, boolean scanned) {
		if (mScan == null) {
			mScan = 0;
		}

		mScan |= 1 << side;
	}

	/**
	 * Returns the raw scan bits.
	 */
	public Integer getScanBits() {
		return mScan;
	}

	/**
	 * Sets the raw scan bits.
	 */
	public void setScanBits(Integer scan) {
		mScan = scan;
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

    	if (mScan != null) {
        	putProp(props, "Scan bits", TextUtil.intToHex(mScan));
    	}

    	putProp(props, "Upgrade", mUpgrade);
    }
}