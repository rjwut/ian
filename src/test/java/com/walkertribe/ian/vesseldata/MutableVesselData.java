package com.walkertribe.ian.vesseldata;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.iface.ArtemisNetworkInterface;

/**
 * Mutable extension of VesselData for testing.
 * @author rjwut
 */
public class MutableVesselData extends VesselData {
	public MutableVesselData(Context ctx) {
		super(ctx, ArtemisNetworkInterface.MIN_VERSION.toString());
	}

	/**
	 * Add this Faction to the VesselData.
	 */
	public void putFaction(Faction faction) {
		factions.add(faction.getId(), faction);
	}

	/**
	 * Add this Vessel to the VesselData.
	 */
	public void putVessel(Vessel vessel) {
		vessels.put(Integer.valueOf(vessel.getId()), vessel);
	}
}
