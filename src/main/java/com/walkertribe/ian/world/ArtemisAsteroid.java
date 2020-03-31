package com.walkertribe.ian.world;

import com.walkertribe.ian.enums.ObjectType;

/**
 * Asteroids
 * @author rjwut
 */
public class ArtemisAsteroid extends BaseArtemisObject {
    public ArtemisAsteroid(int objId) {
        super(objId);
    }

    @Override
    public ObjectType getType() {
        return ObjectType.ASTEROID;
    }
}
