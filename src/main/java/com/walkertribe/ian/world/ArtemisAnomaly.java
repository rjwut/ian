package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.AnomalyType;
import com.walkertribe.ian.enums.BeaconMode;
import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.TextUtil;

/**
 * An anomaly (upgrade pickup, deployed beacon, or space junk)
 * @author rjwut
 */
public class ArtemisAnomaly extends BaseArtemisObject {
	private Integer mScan;
	private AnomalyType mAnomalyType;
	private CreatureType mBeaconType;
	private BeaconMode mBeaconMode;

	public ArtemisAnomaly(int objId) {
		super(objId);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.ANOMALY;
	}

	@Override
	public int getMaxScans() {
	    return 1;
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

		if (scanned) {
	        mScan |= 1 << side;
		} else {
	        mScan &= ~(1 << side);
		}
	}

    @Override
    public int getScanLevel(int side) {
        return isScanned(side).toValue(1, 0, -1);
    }

    @Override
    public void setScanLevel(int side, int scanLevel) {
        setScanned(side, scanLevel != 0);
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
	 * Returns the type of anomaly this is.
	 */
	public AnomalyType getAnomalyType() {
		return mAnomalyType;
	}

	public void setAnomalyType(AnomalyType anomalyType) {
		mAnomalyType = anomalyType;
	}

	/**
	 * The type of creature this beacon affects. The value of this property
	 * should be ignored if the anomaly type is not BEACON.
	 */
	public CreatureType getBeaconType() {
		return mBeaconType;
	}

	public void setBeaconType(CreatureType beaconType) {
		if (beaconType == CreatureType.WRECK) {
			throw new IllegalArgumentException("Can't have beacons for wrecks");
		}

		mBeaconType = beaconType;
	}

	/**
	 * Whether the creature is attracted or repelled by this bacon. The value
	 * of this property should be ignored if the anomaly type is not BEACON.
	 */
	public BeaconMode getBeaconMode() {
		return mBeaconMode;
	}

	public void setBeaconMode(BeaconMode beaconMode) {
		mBeaconMode = beaconMode;
	}

	@Override
    public void updateFrom(ArtemisObject obj) {
        super.updateFrom(obj);
        
        if (obj instanceof ArtemisAnomaly) {
        	ArtemisAnomaly anomaly = (ArtemisAnomaly) obj;

        	if (anomaly.mAnomalyType != null) {
            	mAnomalyType = anomaly.mAnomalyType;
            }

        	if (anomaly.mScan != null) {
        		mScan = anomaly.mScan;
        	}

        	if (anomaly.mBeaconType != null) {
        		mBeaconType = anomaly.mBeaconType;
        	}

        	if (anomaly.mBeaconMode != null) {
        		mBeaconMode = anomaly.mBeaconMode;
        	}
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);

    	if (mScan != null) {
        	putProp(props, "Scan bits", TextUtil.intToHex(mScan));
    	}

    	putProp(props, "Anomaly type", mAnomalyType);
    	putProp(props, "Beacon type", mBeaconType);
    	putProp(props, "Beacon mode", mBeaconMode);
    }
}