package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.model.Model;

/**
 * Various spacefaring creatures (and wrecks)
 */
public class ArtemisCreature extends BaseArtemisOrientable {
	private CreatureType mCreatureType;
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
	public Model getModel(Context ctx) {
		return mCreatureType != null ? mCreatureType.getModel(ctx) : null;
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

            if (!Float.isNaN(cast.mHealth)) {
            	mHealth = cast.mHealth;
            }

            if (!Float.isNaN(cast.mMaxHealth)) {
            	mMaxHealth = cast.mMaxHealth;
            }
        }
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
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "Creature type", mCreatureType);
    	putProp(props, "Health", mHealth);
    	putProp(props, "Max health", mMaxHealth);
    }
}