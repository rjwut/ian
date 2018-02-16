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
    // scan levels... only 2 for now
    public static final byte SCAN_LEVEL_BASIC = 1;
    public static final byte SCAN_LEVEL_FULL  = 2;

    private int mScanLevel = -1;
    private int mSpecial = -1, mSpecialState = -1;
    private BoolState mEnemy = BoolState.UNKNOWN;
    private BoolState mSurrendered = BoolState.UNKNOWN;
    private byte mFleetNumber = (byte) -1;
    private CharSequence mIntel;
    private final float[] mSysDamage = new float[Artemis.SYSTEM_COUNT];

    public ArtemisNpc(int objId) {
        super(objId);

        for (int i = 0; i < Artemis.SYSTEM_COUNT; i++) {
        	mSysDamage[i] = -1;
        }
    }

    @Override
    public ObjectType getType() {
        return ObjectType.NPC_SHIP;
    }

    /**
     * Returns BoolState.TRUE if this ship is an enemy, BoolState.FALSE if it's
     * friendly. Note that this only works in Solo mode.
     * Unspecified: BoolState.UNKNOWN
     */
    public BoolState isEnemy() {
    	return mEnemy;
    }

    public void setEnemy(BoolState enemy) {
    	mEnemy = enemy;
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
     * Returns the ship's fleet number.
     * Unspecified: -1
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

    /**
     * The scan level for this ship.
     * Unspecified: -1
     */
    public int getScanLevel() {
        return mScanLevel;
    }

    public void setScanLevel(int scanLevel) {
        mScanLevel = scanLevel;
    }

    /**
     * Returns true if this ship has been scanned at the given level or higher;
     * false otherwise.
     */
    public boolean isScanned(byte scanLevel) {
        return mScanLevel >= scanLevel;
    }

    /**
     * The intel String for this ship.
     * Unspecified: null
     */
    public CharSequence getIntel() {
    	return mIntel;
    }

    public void setIntel(CharSequence intel) {
    	mIntel = intel;
    }

    /**
     * The percentage of damage sustained by a particular system, expressed as
     * a value between 0 and 1.
     * Unspecified: -1
     */
    public float getSystemDamage(ShipSystem sys) {
    	return mSysDamage[sys.ordinal()];
    }

    public void setSystemDamage(ShipSystem sys, float value) {
    	mSysDamage[sys.ordinal()] = value;
    }

    @Override
    public void updateFrom(ArtemisObject npc, Context ctx) {
        super.updateFrom(npc, ctx);
        
        // it SHOULD be an ArtemisNpc
        if (npc instanceof ArtemisNpc) {
            ArtemisNpc cast = (ArtemisNpc) npc;
            BoolState enemy = cast.isEnemy();

            if (BoolState.isKnown(enemy)) {
            	mEnemy = enemy;
            }

            BoolState surrendered = cast.isSurrendered();

            if (BoolState.isKnown(surrendered)) {
            	mSurrendered = surrendered;
            }

            if (cast.mFleetNumber != (byte) -1) {
            	setFleetNumber(cast.mFleetNumber);
            }

            if (cast.mScanLevel != -1) {
                setScanLevel(cast.mScanLevel);
            }

            if (cast.mSpecial != -1) {
            	setSpecialBits(cast.mSpecial);
            }

            if (cast.mSpecialState != -1) {
                setSpecialStateBits(cast.mSpecialState);
            }

            if (cast.mIntel != null) {
            	setIntel(cast.mIntel);
            }

            for (int i = 0; i < mSysDamage.length; i++) {
            	float value = cast.mSysDamage[i];

            	if (value != -1) {
                    mSysDamage[i] = value;
            	}
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props,
			boolean includeUnspecified) {
    	super.appendObjectProps(props, includeUnspecified);
    	putProp(props, "Scan level", mScanLevel, -1, includeUnspecified);

    	if (mSpecial != -1) {
    		String str = Util.enumSetToString(SpecialAbility.fromValue(mSpecial));
    		props.put("Specials", str.length() > 0 ? str : "NONE");
    	} else if (includeUnspecified) {
        	props.put("Specials", "UNKNOWN");
    	}

    	if (mSpecialState != -1) {
    		String str = Util.enumSetToString(SpecialAbility.fromValue(mSpecialState));
    		props.put("Specials active", str.length() > 0 ? str : "NONE");
    	} else if (includeUnspecified) {
        	props.put("Specials active", "UNKNOWN");
    	}

    	putProp(props, "Is enemy", mEnemy, includeUnspecified);
    	putProp(props, "Surrendered", mSurrendered, includeUnspecified);
    	putProp(props, "Fleet number", mFleetNumber, -1, includeUnspecified);
    	putProp(props, "Intel", mIntel, includeUnspecified);
    	ShipSystem[] systems = ShipSystem.values();

    	for (int i = 0; i < mSysDamage.length; i++) {
    		putProp(props, "Damage: " + systems[i], mSysDamage[i],
    				-1, includeUnspecified);
    	}
    }
}