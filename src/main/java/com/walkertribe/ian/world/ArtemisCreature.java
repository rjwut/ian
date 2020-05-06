package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.TextUtil;

/**
 * Various spacefaring creatures (and wrecks)
 */
public class ArtemisCreature extends BaseArtemisOrientable {
	private CreatureType mCreatureType;
    private Integer mScan;
	private float mHealth = Float.NaN;
	private float mMaxHealth = Float.NaN;

    public ArtemisCreature(int objId) {
        super(objId);
    }

	@Override
	public ObjectType getType() {
		return ObjectType.CREATURE;
	}

    @Override
    public int getMaxScans() {
        return 1;
    }

    /**
     * Whether the given side has scanned this creature.
     * Unspecified: UNKNOWN
     */
    public BoolState isScanned(int side) {
        if (mScan == null) {
            return BoolState.UNKNOWN;
        }

        return BoolState.from((mScan & (1 << side)) != 0);
    }

    /**
     * Sets whether the given side has scanned this creature.
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

	@Override
	public Model getModel(Context ctx) {
		return mCreatureType != null ? mCreatureType.getModel(ctx) : null;
	}

    /**
     * Returns the type of creature this is.
     */
    public CreatureType getCreatureType() {
    	return mCreatureType;
    }

    public void setCreatureType(CreatureType creatureType) {
    	mCreatureType = creatureType;
    }

    /**
     * The health level of this creature.
     * Unspecified: Float.NaN
     */
	public float getHealth() {
		return mHealth;
	}

	public void setHealth(float health) {
		mHealth = health;
	}

    /**
     * The maximum health level of this creature.
     * Unspecified: Float.NaN
     */
	public float getMaxHealth() {
		return mMaxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		mMaxHealth = maxHealth;
	}

    @Override
    public void updateFrom(ArtemisObject obj) {
        super.updateFrom(obj);
        
        if (obj instanceof ArtemisCreature) {
            ArtemisCreature cast = (ArtemisCreature) obj;

            CreatureType creatureType = cast.getCreatureType();

            if (creatureType != null) {
                setCreatureType(creatureType);
            }

            if (cast.mScan != null) {
                mScan = cast.mScan;
            }

            if (!Float.isNaN(cast.mHealth)) {
                mHealth = cast.mHealth;
            }

            if (!Float.isNaN(cast.mMaxHealth)) {
                mMaxHealth = cast.mMaxHealth;
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "Creature type", mCreatureType);
    	putProp(props, "Health", mHealth);
    	putProp(props, "Max health", mMaxHealth);

    	if (mScan != null) {
            putProp(props, "Scan bits", TextUtil.intToHex(mScan));
        }
    }
}