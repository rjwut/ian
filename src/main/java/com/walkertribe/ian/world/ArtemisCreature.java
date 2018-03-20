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
	private float mHealth = Float.MIN_VALUE;
	private float mMaxHealth = Float.MIN_VALUE;

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

            if (cast.mHealth != Float.MIN_VALUE) {
            	mHealth = cast.mHealth;
            }

            if (cast.mMaxHealth != Float.MIN_VALUE) {
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
     */
	public float getHealth() {
		return mHealth;
	}

	public void setHealth(float health) {
		mHealth = health;
	}

    /**
     * The maximum health level of this creature.
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