package com.walkertribe.ian.vesseldata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.OrdnanceType;
import com.walkertribe.ian.model.Model;

/**
 * Corresponds to the &lt;vessel&gt; element in vesselData.xml. Note that this
 * represents an entire class of ships, not an individual one.
 * @author rjwut
 */
public class Vessel {
	private Context ctx;
	private int id;
	private int side;
	private String name;
	String description;
	private Set<String> attributes;
	List<Art> artList = new ArrayList<Art>();
	private String dxsPaths;
	String internalDataFile;
	float scale;
	int pushRadius;
	int foreShields;
	int aftShields;
	int playerShields;
	float turnRate;
	float topSpeed;
	float shipEfficiency;
	float warpEfficiency;
	float jumpEfficiency;
	int fleetAiCommonality;
	int bayCount;
	int fighterCount;
	int bomberCount;
	float productionCoeff;
	List<BeamPort> beamPorts = new ArrayList<BeamPort>();
	List<VesselPoint> torpedoTubes = new ArrayList<VesselPoint>();
	Map<OrdnanceType, Integer> torpedoStorage = new LinkedHashMap<OrdnanceType, Integer>();
	int totalTorpedoStorage;
	List<WeaponPort> dronePorts = new ArrayList<WeaponPort>();
	List<WeaponPort> baseTorpedoPorts = new ArrayList<WeaponPort>();
	List<VesselPoint> enginePorts = new ArrayList<VesselPoint>();
	List<VesselPoint> impulsePoints = new ArrayList<VesselPoint>();
	List<VesselPoint> maneuverPoints = new ArrayList<VesselPoint>();

	Vessel(Context ctx, int uniqueID, int side, String className, String broadType) {
		this.ctx = ctx;
		id = uniqueID;
		this.side = side;
		name = className;
		attributes = VesselAttribute.build(broadType);

		for (OrdnanceType type : OrdnanceType.values()) {
			torpedoStorage.put(type, Integer.valueOf(0));
		}
	}

	/**
	 * Returns the Vessel's ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the Vessel's Faction ID. 
	 */
	public int getSide() {
		return side;
	}

	/**
	 * Returns the Faction to which this Vessel belongs.
	 */
	public Faction getFaction() {
		return ctx.getVesselData().getFaction(side);
	}

	/**
	 * Returns this Vessel's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a short description of this Vessel.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns an array of this Vessel's attributes.
	 */
	public String[] getAttributes() {
		return attributes.toArray(new String[attributes.size()]);
	}

	/**
	 * Returns true if this Vessel has all the given attributes; false
	 * otherwise.
	 */
	public boolean is(String... attrs) {
		for (String attr : attrs) {
			if (!attributes.contains(attr)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns an array of Art objects containing the asset filenames for this
	 * Vessel.
	 */
	public Art[] getArt() {
		return artList.toArray(new Art[artList.size()]);
	}

	/**
	 * Returns the Model for this Vessel. If the .dxs files have not already
	 * been loaded and parsed, this will cause that to happen now, and cache
	 * the data for later re-use.
	 */
	public Model getModel() {
		if (dxsPaths == null) {
			StringBuilder b = new StringBuilder();

			for (Art art : artList) {
				if (b.length() != 0) {
					b.append(',');
				}

				b.append(art.getMeshFile());
			}

			dxsPaths = b.toString();
		}

		return ctx.getModel(dxsPaths);
	}

	/**
	 * Returns the filename of the internal data (.snt) file for this Vessel,
	 * if any.
	 */
	public String getInternalDataFile() {
		return internalDataFile;
	}

	/**
	 * Returns the VesselInternals for this Vessel. If the .snt file has not
	 * already been loaded and parsed, this will cause that to happen onw, and
	 * cache the data for later re-use.
	 */
	public VesselInternals getInternals() {
		return internalDataFile != null ? ctx.getInternals(internalDataFile) : null;
	}

	/**
	 * Returns this Vessel's scale value. This presumably controls how large it
	 * is.
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Returns this Vessel's push radius. This presumably how close other
	 * objects can get before they are considered to have collided with this
	 * Vessel.
	 */
	public int getPushRadius() {
		return pushRadius;
	}

	/**
	 * Returns the initial strength of this Vessel's forward shields.
	 */
	public int getForeShields() {
		return foreShields;
	}

	/**
	 * Returns the initial strength of this Vessel's aft shields.
	 */
	public int getAftShields() {
		return aftShields;
	}

	/**
	 * Returns the initial strength of this Vessel's "player" shields. (This
	 * appears to only apply to single-seat craft.)
	 */
	public int getPlayerShields() {
		return playerShields;
	}

	/**
	 * Returns this Vessel's turn rate.
	 */
	public float getTurnRate() {
		return turnRate;
	}

	/**
	 * Returns this Vessel's top (impulse) speed.
	 */
	public float getTopSpeed() {
		return topSpeed;
	}

	/**
	 * Returns this Vessel's efficiency rating.
	 */
	public float getShipEfficiency() {
		return shipEfficiency;
	}

	/**
	 * Returns this Vessel's warp efficiency rating.
	 */
	public float getWarpEfficiency() {
		return warpEfficiency;
	}

	/**
	 * Returns this Vessel's jump efficiency rating.
	 */
	public float getJumpEfficiency() {
		return jumpEfficiency;
	}

	/**
	 * Returns this Vessel's fleet AI commonality value. It is unknown what
	 * exactly this value does.
	 */
	public int getFleetAiCommonality() {
		return fleetAiCommonality;
	}

	/**
	 * Returns the number of bays this Vessel has. Only Vessels that were declared with the
	 * &lt;carrierload&gt; element will have bays. Note that a vessel does not have to have
	 * VesselAttribute.CARRIER to have bays.
	 */
	public int getBayCount() {
		return fighterCount;
	}

	/**
	 * Returns the number of fighters this Vessel has. Only Vessels that were declared with the
	 * &lt;carrierload&gt; (player ships) elements will have fighters. Note that a vessel does
	 * not have to have VesselAttribute.CARRIER to have fighters.
	 */
	public int getFighterCount() {
		return fighterCount;
	}

	/**
	 * Returns the number of bombers this Vessel has. Only Vessels that were declared with the
	 * &lt;carrierload&gt; element will have bombers.
	 */
	public int getBomberCount() {
		return bomberCount;
	}

	/**
	 * Returns the base production coefficient. This value affects how quickly
	 * the base produces new ordnance.
	 */
	public float getProductionCoeff() {
		return productionCoeff;
	}

	/**
	 * Returns an array of BeamPort objects describing the beams with which this
	 * Vessel is equipped.
	 */
	public BeamPort[] getBeamPorts() {
		return beamPorts.toArray(new BeamPort[beamPorts.size()]);
	}

	/**
	 * Returns an array of VesselPoint objects describing the locations of the
	 * Vessel's torpedo tubes.
	 */
	public VesselPoint[] getTorepedoTubes() {
		return torpedoTubes.toArray(new BeamPort[torpedoTubes.size()]);
	}

	/**
	 * Returns the number of units of the given OrdnanceType this Vessel can
	 * carry.
	 */
	public int getTorpedoStorage(OrdnanceType type) {
		return torpedoStorage.get(type).intValue();
	}

	/**
	 * Returns true if this Vessel is capable of launching ordnance; false
	 * otherwise. To launch ordnance, a player Vessel must storage space for at
	 * least one torpedo and have at least one torpedo tube or be a single-seat
	 * craft (which doesn't use the tube loading/unloading system). Other
	 * Vessel types must have at least one drone port or base torpedo port.
	 */
	public boolean canLaunchOrdnance() {
		if (is(VesselAttribute.PLAYER)) {
			return totalTorpedoStorage > 0 && (!torpedoTubes.isEmpty() || is(VesselAttribute.FIGHTER));
		}

		return !dronePorts.isEmpty() || !baseTorpedoPorts.isEmpty();
	}

	/**
	 * Returns an array of WeaponPoint objects describing the locations of the
	 * Vessel's drone launchers.
	 */
	public WeaponPort[] getDronePorts() {
		return dronePorts.toArray(new WeaponPort[dronePorts.size()]);
	}

	/**
	 * Returns an array of WeaponPoint objects describing the locations of the
	 * Vessel's base torpedo launchers. As of this writing, only the Command
	 * Station base has this port in the stock Artemis install; player vessels
	 * have torpedo tubes instead, and no other vessels launch torpedoes
	 * (although Torgoth drones are basically the same).
	 */
	public WeaponPort[] getBaseTorpedoPorts() {
		return baseTorpedoPorts.toArray(new WeaponPort[baseTorpedoPorts.size()]);
	}

	/**
	 * Returns an array of VesselPoint objects describing the locations of the
	 * Vessel's engine ports.
	 */
	public VesselPoint[] getEnginePorts() {
		return enginePorts.toArray(new VesselPoint[enginePorts.size()]);
	}

	/**
	 * Returns an array of VesselPoint objects describing the locations of the
	 * Vessel's impulse points.
	 */
	public VesselPoint[] getImpulsePoints() {
		return impulsePoints.toArray(new VesselPoint[impulsePoints.size()]);
	}

	/**
	 * Returns an array of VesselPoint objects describing the locations of the
	 * Vessel's maneuver points.
	 */
	public VesselPoint[] getManeuverPoints() {
		return maneuverPoints.toArray(new VesselPoint[maneuverPoints.size()]);
	}
}