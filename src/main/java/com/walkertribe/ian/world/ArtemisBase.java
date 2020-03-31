package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * Bases
 */
public class ArtemisBase extends BaseArtemisShielded {
    public ArtemisBase(int objId) {
        super(objId);
    }

    public ArtemisBase(int objId, Vessel vessel) {
        super(objId);

        if (vessel != null) {
            setVessel(vessel);
            setArtemisClass(vessel.getName());
            setRace(vessel.getFaction().getName());
            setShieldsFront(vessel.getForeShields());
            setShieldsFrontMax(vessel.getForeShields());
        }
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