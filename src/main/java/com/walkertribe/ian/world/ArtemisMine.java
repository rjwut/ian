package com.walkertribe.ian.world;

import com.walkertribe.ian.enums.ObjectType;

/**
 * Mines
 * @author rjwut
 */
public class ArtemisMine extends BaseArtemisObject {
    public ArtemisMine(int objId) {
        super(objId);
    }

    @Override
    public ObjectType getType() {
        return ObjectType.MINE;
    }
}
