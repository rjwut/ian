package com.walkertribe.ian.world;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.SpecialAbility;
import com.walkertribe.ian.enums.FactionAttribute;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.TextUtil;
import com.walkertribe.ian.util.Util;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * <p>
 * An NPC ship; they may have special abilities, and can be scanned.
 * </p>
 * <p>
 * The Artemis server sometimes sends garbage data for the special abilities
 * properties when a vessel does not have special abilities. The
 * {@link #getSpecialBits()} and {@link #getSpecialStateBits()} methods
 * preserve the exact data sent by the server. The other methods related to
 * special abilities will attempt to reconcile this with the information parsed
 * from vesselData.xml. If the vessel spec indicates that it can't have special
 * abilities, then these methods will never report that it has them, regardless
 * of what is reported by received packets. If the vessel cannot be determined
 * (because the hull ID is unspecified or does not correspond to a known
 * vessel), these methods will indicate that it cannot reliably make a
 * determination.
 * </p>
 * @author dhleong
 */
public class ArtemisNpc extends BaseArtemisShip {
	public static final int MAX_SCAN_LEVEL = 2;

    private Integer[] mScanLevels = new Integer[MAX_SCAN_LEVEL];
    private int mSpecial = -1, mSpecialState = -1;
    private BoolState mSurrendered = BoolState.UNKNOWN;
    private byte mFleetNumber = Byte.MIN_VALUE;
    private float mTargetX = Float.NaN;
    private float mTargetY = Float.NaN;
    private float mTargetZ = Float.NaN;
    private BoolState mTagged = BoolState.UNKNOWN;
    private final float[] mSysDamage = new float[Artemis.SYSTEM_COUNT];

    public ArtemisNpc(int objId) {
        super(objId);
        init();
    }

    public ArtemisNpc(int objId, Vessel vessel) {
        super(objId, vessel);
        init();
    }

    private void init() {
        for (int i = 0; i < Artemis.SYSTEM_COUNT; i++) {
            mSysDamage[i] = Float.NaN;
        }
    }

    @Override
    public ObjectType getType() {
        return ObjectType.NPC_SHIP;
    }

    /**
     * Returns BoolState.TRUE if this ship has surrendered, and BoolState.FALSE
     * if it hasn't.
     * Unspecified: BoolState.UNKNOWN
     */
    public BoolState isSurrendered() {
    	return mSurrendered;
    }

    public void setSurrendered(BoolState surrendered) {
    	mSurrendered = surrendered;
    }

    /**
     * Returns the ship's fleet number. -1 means no fleet.
     * Unspecified: Byte.MIN_VALUE
     */
    public byte getFleetNumber() {
    	return mFleetNumber;
    }

    public void setFleetNumber(byte fleetNumber) {
    	mFleetNumber = fleetNumber;
    }

    /**
     * Returns a Set containing the SpecialAbility values that pertain to this
     * ship, or null if the special abilities are unspecified or cannot be
     * determined. See the class documentation about special abilities for more
     * information.
     * Unspecified: null
     */
    public Set<SpecialAbility> getSpecialAbilities(Context ctx) {
    	if (mSpecial == -1) {
    		return null; // specials are unspecified
    	}

    	Vessel vessel = getVessel(ctx);

    	if (vessel == null) {
    		return null; // Vessel is unspecified or unrecognized
    	}

    	if (!vessel.getFaction().is(FactionAttribute.HASSPECIALS)) {
    		return new HashSet<SpecialAbility>();
    	}

    	return SpecialAbility.fromValue(mSpecial);
    }

    /**
     * Returns .TRUE if this ship has the specified special ability, .FALSE if
     * it does not, and .UNKNOWN if it cannot be determined. See the class
     * documentation about special abilities for more information.
     */
    public BoolState hasSpecialAbility(SpecialAbility ability, Context ctx) {
    	if (mSpecial == -1) {
    		return BoolState.UNKNOWN; // specials are unspecified
    	}

    	Vessel vessel = getVessel(ctx);

    	if (vessel == null) {
    		return BoolState.UNKNOWN; // Vessel is unspecified or unrecognized
    	}

    	if (!vessel.getFaction().is(FactionAttribute.HASSPECIALS)) {
    		return BoolState.FALSE;
    	}

    	return BoolState.from(ability.on(mSpecial));
    }

    /**
     * Returns .TRUE if this ship is using the specified special ability,
     * .FALSE if it is not, and .UNKNOWN if it cannot be determined. See the
     * class documentation about special abilities for more information.
     */
    public BoolState isUsingSpecialAbility(SpecialAbility ability, Context ctx) {
    	if (mSpecialState == -1) {
    		return BoolState.UNKNOWN; // specials are unspecified
    	}

    	Vessel vessel = getVessel(ctx);

    	if (vessel == null) {
    		return BoolState.UNKNOWN; // Vessel is unspecified or unrecognized
    	}

    	if (!vessel.getFaction().is(FactionAttribute.HASSPECIALS)) {
    		return BoolState.FALSE;
    	}

        return BoolState.from(ability.on(mSpecialState));
    }

    /**
     * Returns the bits for the special ability property. See the class
     * documentation about special abilities for more information.
     * Unspecified: -1
     */
    public int getSpecialBits() {
    	return mSpecial;
    }

    public void setSpecialBits(int special) {
        mSpecial = special;
    }

    /**
     * Returns the bits for special ability state property (what abilities are
     * currently active). See the class documentation about special abilities
     * for more information.
     * Unspecified: -1
     */
    public int getSpecialStateBits() {
    	return mSpecialState;
    }

    public void setSpecialStateBits(int special) {
        mSpecialState = special;
    }

    @Override
    public int getMaxScans() {
        return MAX_SCAN_LEVEL;
    }

    @Override
    public int getScanLevel(int side) {
        if (side == -1) {
            return -1;
        }

        if (getSide() == side) {
            return MAX_SCAN_LEVEL; // don't need to scan objects on the same side
        }

        int scanLevel = 0;
    	int bit = 1 << side;

    	for (Integer level : mScanLevels) {
    		if (level == null) {
    			return -1;
    		}

    		if ((level & bit) == 0) {
    			break;
    		}

    		scanLevel++;
    	}

    	return scanLevel;
    }

    @Override
    public void setScanLevel(int side, int scanLevel) {
    	if (scanLevel == -1) {
    		return;
    	}

    	if (scanLevel < -1 || scanLevel > MAX_SCAN_LEVEL) {
    		throw new IllegalArgumentException("Invalid scan level");
    	}

    	int bit = 1 << side;

    	for (int i = 0; i < scanLevel; i++) {
    		Integer level = mScanLevels[i];

    		if (level == null) {
    			level = 0;
    		}

    		level |= bit;
    		mScanLevels[i] = level;
    	}
    }

    /**
     * Returns the raw bits for the indicated scan level.
     */
    public Integer getScanLevelBits(int level) {
    	return mScanLevels[level - 1];
    }

    /**
     * Sets the raw bits for the indicated scan level.
     */
    public void setScanLevelBits(int level, int bits) {
    	mScanLevels[level - 1] = bits;
    }

    /**
     * Target's X-coordinate
     * Unspecified: Float.NaN
     */
    public float getTargetX() {
    	return mTargetX;
    }

    public void setTargetX(float targetX) {
    	mTargetX = targetX;
    }

    /**
     * Target's Y-coordinate
     * Unspecified: Float.NaN
     */
    public float getTargetY() {
    	return mTargetY;
    }

    public void setTargetY(float targetY) {
    	mTargetY = targetY;
    }

    /**
     * Target's Z-coordinate
     * Unspecified: Float.NaN
     */
    public float getTargetZ() {
    	return mTargetZ;
    }

    public void setTargetZ(float targetZ) {
    	mTargetZ = targetZ;
    }

    /**
     * Is the NPC tagged?
     * Unspecified: UNKNOWN
     */
    public BoolState isNpcTagged() {
    	return mTagged;
    }

    public void setNpcTagged(BoolState tagged) {
    	mTagged = tagged;
    }

    /**
     * The percentage of damage sustained by a particular system, expressed as
     * a value between 0 and 1.
     * Unspecified: Float.NaN
     */
    public float getSystemDamage(ShipSystem sys) {
    	return mSysDamage[sys.ordinal()];
    }

    public void setSystemDamage(ShipSystem sys, float value) {
    	mSysDamage[sys.ordinal()] = value;
    }

    @Override
    public void updateFrom(ArtemisObject npc) {
        super.updateFrom(npc);
        
        // it SHOULD be an ArtemisNpc
        if (npc instanceof ArtemisNpc) {
            ArtemisNpc cast = (ArtemisNpc) npc;

            BoolState surrendered = cast.isSurrendered();

            if (BoolState.isKnown(surrendered)) {
            	mSurrendered = surrendered;
            }

            if (cast.mFleetNumber != Byte.MIN_VALUE) {
            	setFleetNumber(cast.mFleetNumber);
            }

            for (int i = 0; i < MAX_SCAN_LEVEL; i++) {
            	Integer scanLevel = cast.mScanLevels[i];

            	if (scanLevel != null) {
            		mScanLevels[i] = scanLevel;
            	}
            }

            if (cast.mSpecial != -1) {
            	setSpecialBits(cast.mSpecial);
            }

            if (cast.mSpecialState != -1) {
                setSpecialStateBits(cast.mSpecialState);
            }

            if (!Float.isNaN(cast.mTargetX)) {
            	mTargetX = cast.mTargetX;
            }

            if (!Float.isNaN(cast.mTargetY)) {
            	mTargetY = cast.mTargetY;
            }

            if (!Float.isNaN(cast.mTargetZ)) {
            	mTargetZ = cast.mTargetZ;
            }

            if (BoolState.isKnown(cast.mTagged)) {
            	mTagged = cast.mTagged;
            }

            for (int i = 0; i < mSysDamage.length; i++) {
            	float value = cast.mSysDamage[i];

            	if (!Float.isNaN(value)) {
                    mSysDamage[i] = value;
            	}
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);

    	for (int i = 0; i < MAX_SCAN_LEVEL; i++) {
    		Integer scanLevel = mScanLevels[i];

    		if (scanLevel != null) {
        		putProp(props, "Scan level " + (i + 1) + " bits", TextUtil.intToHex(mScanLevels[i]));
    		}
    	}

    	if (mSpecial != -1) {
    		String str = Util.enumSetToString(SpecialAbility.fromValue(mSpecial));
    		props.put("Specials", str.length() > 0 ? str : "NONE");
    	}

    	if (mSpecialState != -1) {
    		String str = Util.enumSetToString(SpecialAbility.fromValue(mSpecialState));
    		props.put("Specials active", str.length() > 0 ? str : "NONE");
    	}

    	putProp(props, "Surrendered", mSurrendered);
    	putProp(props, "Fleet number", mFleetNumber, Byte.MIN_VALUE);
    	putProp(props, "Target X", mTargetX);
    	putProp(props, "Target Y", mTargetY);
    	putProp(props, "Target Z", mTargetZ);
    	putProp(props, "Tagged", mTagged);
    	ShipSystem[] systems = ShipSystem.values();

    	for (int i = 0; i < mSysDamage.length; i++) {
    		putProp(props, "Damage: " + systems[i], mSysDamage[i]);
    	}
    }
}