package com.walkertribe.ian.world;

import com.walkertribe.ian.enums.ObjectType;

/**
 * There are many "generic" objects which are very similar in implementation.
 * They are all handled by this class. Specifically, the objects implemented by
 * this class are: mines, torpedoes, black holes and asteroids.
 * @author dhleong
 */
public class ArtemisGenericObject extends BaseArtemisObject {
    private ObjectType mType;

    public ArtemisGenericObject(int objId) {
        super(objId);
    }

    @Override
    public ObjectType getType() {
        return mType;
    }

    public void setType(ObjectType type) {
    	if (type == null || !type.isCompatible(this)) {
    		throw new IllegalArgumentException(
    				"Invalid object type for " + getClass().getName() + ": " +
    				type
    		);
    	}

    	mType = type;
    }
}