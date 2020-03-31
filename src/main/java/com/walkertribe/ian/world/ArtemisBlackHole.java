package com.walkertribe.ian.world;

import com.walkertribe.ian.enums.ObjectType;

/**
 * Black holes
 * @author rjwut
 */
public class ArtemisBlackHole extends BaseArtemisObject {
    public ArtemisBlackHole(int objId) {
        super(objId);
    }

    @Override
    public ObjectType getType() {
        return ObjectType.BLACK_HOLE;
    }
}
