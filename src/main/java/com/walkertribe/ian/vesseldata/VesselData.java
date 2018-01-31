package com.walkertribe.ian.vesseldata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.util.Version;

/**
 * Contains all the information extracted from the vesselData.xml file.
 * @author rjwut
 */
public class VesselData {
	Version version;
	List<Faction> factions = new ArrayList<Faction>();
	Map<Integer, Vessel> vessels = new LinkedHashMap<Integer, Vessel>();
	private Context ctx;

	VesselData (Context ctx, String version) {
		this.ctx = ctx;
		this.version = new Version(version);
	}

	/**
	 * Returns the Context that was used to load this VesselData.
	 */
	Context getContext() {
		return ctx;
	}

	/**
	 * Returns the version of Artemis reported by vesselData.xml. Note that this
	 * does not necessarily match the version reported by the protocol; the
	 * version in vesselData.xml is known to lag behind the actual version
	 * number.
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * Returns the Faction represented by the given ID.
	 */
	public Faction getFaction(int id) {
		return factions.get(id);
	}

	/**
	 * Returns the Vessel represented by the given ID.
	 */
	public Vessel getVessel(int id) {
		return vessels.get(Integer.valueOf(id));
	}

	/**
	 * Iterates all the Factions in this object.
	 */
	public Iterator<Faction> factionIterator() {
		return factions.iterator();
	}

	/**
	 * Iterates all the Vessels in this object.
	 */
	public Iterator<Vessel> vesselIterator() {
		return vessels.values().iterator();
	}

	/**
	 * Preloads all of the Model objects into memory. If you do not call this
	 * method, then each Model object will be loaded into memory on demand
	 * instead.
	 */
	public void preloadModels() {
		for (Vessel vessel : vessels.values()) {
			vessel.getModel();
		}

		for (ObjectType objectType : ObjectType.values()) {
			objectType.getModel(ctx);
		}

		for (CreatureType creatureType : CreatureType.values()) {
			creatureType.getModel(ctx);
		}
	}

	/**
	 * Preloads all of the VesselInternals objects into memory. If you do not
	 * call this method, then each VesselInternals object will be loaded into
	 * memory on demand instead.
	 */
	public void preloadInternals() {
		for (Vessel vessel : vessels.values()) {
			vessel.getInternals();
		}
	}
}
