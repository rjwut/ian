package com.walkertribe.ian.vesseldata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.walkertribe.ian.enums.OrdnanceType;
import com.walkertribe.ian.enums.VesselAttribute;
import com.walkertribe.ian.model.Model;

/**
 * Corresponds to the <vessel> element in vesselData.xml. Note that this
 * represents an entire class of ships, not an individual one.
 * @author rjwut
 */
public class Vessel {
	private int id;
	private int side;
	private String name;
	String description;
	private Set<VesselAttribute> attributes;
	List<Art> artList = new ArrayList<Art>();
	VesselInternals internals;
	float scale;
	int pushRadius;
	int foreShields;
	int aftShields;
	float turnRate;
	float topSpeed;
	float efficiency;
	int fleetAiCommonality;
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

	Vessel(int uniqueID, int side, String className, String broadType) {
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
		return VesselData.get().getFaction(side);
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
	 * Returns an array of this Vessel's VesselAttributes.
	 */
	public VesselAttribute[] getAttributes() {
		return attributes.toArray(new VesselAttribute[attributes.size()]);
	}

	/**
	 * Returns true if this Vessel has all the given VesselAttributes; false
	 * otherwise.
	 */
	public boolean is(VesselAttribute... attrs) {
		for (VesselAttribute attr : attrs) {
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
	 * Returns the Model for this Vessel.
	 */
	public Model getModel() {
		StringBuilder b = new StringBuilder();

		for (Art art : artList) {
			if (b.length() != 0) {
				b.append(',');
			}

			b.append(art.getMeshFile());
		}

		return VesselData.getModel(b.toString());
	}

	/**
	 * Returns the VesselInternals object describing the system node structure
	 * for this Vessel, or null if there is none. (Only player ships will have
	 * a VesselInternals object.)
	 */
	public VesselInternals getInternals() {
		return internals;
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
	public float getEfficiency() {
		return efficiency;
	}

	/**
	 * Returns this Vessel's fleet AI commonality value. It is unknown what
	 * exactly this value does.
	 */
	public int getFleetAiCommonality() {
		return fleetAiCommonality;
	}

	/**
	 * Returns the number of fighters this Vessel has. Only Vessels that were
	 * declared with the <carrierload> (player ships) or <carrier> (enemy ships)
	 * elements will have fighters. Note that a vessel does not have to have
	 * VesselAttribute.CARRIER to have fighters.
	 */
	public int getFighterCount() {
		return fighterCount;
	}

	/**
	 * Returns the number of bombers this Vessel has. Only Vessels that were
	 * declared with the <carrierload> element will have bombers. As of this
	 * writing, the only Vessel to have bombers is the TSN Medium Carrier.
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
	 * least one torpedo and have at least one torpedo tube or be a fighter
	 * (which doesn't use the tube loading/unloading system). Other Vessel types
	 * must have at least one drone port or base torpedo port.
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