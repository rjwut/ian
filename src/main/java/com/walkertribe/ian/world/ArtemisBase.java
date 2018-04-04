package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.ObjectType;

/**
 * Bases
 */
public class ArtemisBase extends BaseArtemisShielded {
	public ArtemisBase(int objId) {
        super(objId);
    }

	@Override
    public ObjectType getType() {
        return ObjectType.BASE;
    }

    @Override
    public void updateFrom(ArtemisObject obj) {
        super.updateFrom(obj);
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    }
}