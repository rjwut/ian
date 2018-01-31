package com.walkertribe.ian.world;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.AlertStatus;
import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.enums.MainScreenView;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.OrdnanceType;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.enums.TargetingMode;
import com.walkertribe.ian.enums.TubeState;
import com.walkertribe.ian.enums.Upgrade;
import com.walkertribe.ian.enums.VesselAttribute;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * A player ship.
 * @author dhleong
 */
public class ArtemisPlayer extends BaseArtemisShip {
	public class Tube {
		private float secondsLeft = -1f;
		private TubeState state;
		private byte contents;
	}

	public class UpgradeStatus {
		private BoolState active;
		private byte count = -1;
		private int secondsLeft = -1;
	}

	private TargetingMode mTargetingMode;
	private AlertStatus mAlertStatus;
    private BoolState mShields;
    private int mShipNumber = -1;
    private final float[] mHeat = new float[Artemis.SYSTEM_COUNT];
    private final float[] mSystems = new float[Artemis.SYSTEM_COUNT];
    private final int[] mCoolant = new int[Artemis.SYSTEM_COUNT];
    private final int[] mTorpedos = new int[OrdnanceType.COUNT];
    private final Tube[] mTubes = new Tube[Artemis.MAX_TUBES];
    private float mEnergy = -1;
    private int mDockingBase = -1;
    private MainScreenView mMainScreen;
    private byte mAvailableCoolantOrMissiles = -1;
    private int mWeaponsTarget = -1;
    private byte mWarp = -1;
    private BeamFrequency mBeamFreq;
    private DriveType mDriveType;
    private BoolState mReverse;
    private int mScienceTarget = -1;
    private float mScanProgress = -1;
    private int mCaptainTarget = -1;
    private int mScanningId = -1;
    private Map<Upgrade, UpgradeStatus> mUpgrades = new LinkedHashMap<Upgrade, UpgradeStatus>(Upgrade.ACTIVATION_UPGRADE_COUNT);
    private int mCapitalShipId = -1;
    private int mAccentColor = -1;

    public ArtemisPlayer(int objId) {
        super(objId);

        // pre-fill
        Arrays.fill(mHeat, -1);
        Arrays.fill(mSystems, -1);
        Arrays.fill(mCoolant, -1);
        Arrays.fill(mTorpedos, -1);

        for (int i = 0; i < Artemis.MAX_TUBES; i++) {
        	mTubes[i] = new Tube();
        }

        for (Upgrade upgrade : Upgrade.activation()) {
        	mUpgrades.put(upgrade, new UpgradeStatus());
        }
    }

    @Override
    public ObjectType getType() {
        return ObjectType.PLAYER_SHIP;
    }

    /**
     * The ship's energy reserves.
     * Unspecified: -1
     */
    public float getEnergy() {
        return mEnergy;
    }

    public void setEnergy(float energy) {
        mEnergy = energy;
    }

    /**
     * Get this ship's player ship number. Note that this value is one-based, so
     * the vessel that is named Artemis will have a ship number of 1.
     * Unspecified: -1
     * @return int in [1,Artemis.SHIP_COUNT], or -1 if undefined
     */
    public int getShipNumber() {
        return mShipNumber == -1 ? -1 : mShipNumber;
    }

    public void setShipNumber(int shipNumber) {
    	mShipNumber = shipNumber;
    }

    /**
     * The amount of coolant allocated to the given system.
     * Unspecified: -1
     */
    public int getSystemCoolant(ShipSystem sys) {
        return mCoolant[sys.ordinal()];
    }

    public void setSystemCoolant(ShipSystem sys, int coolant) {
    	if (coolant < 0 || coolant > Artemis.MAX_COOLANT_PER_SYSTEM) {
    		throw new IllegalArgumentException("Invalid coolant value (" + coolant + ") for " + sys);
    	}

    	mCoolant[sys.ordinal()] = coolant;
    }

    /**
     * The energy allocation level for a system, as a value between 0 (no energy
     * allocated) and 1 (maximum energy allocated).
     * Unspecified: -1
     */
    public float getSystemEnergy(ShipSystem sys) {
        return mSystems[sys.ordinal()];
    }

    public void setSystemEnergy(ShipSystem sys, float energy) {
        if (energy < 0f || energy > 1f) {
            throw new IllegalArgumentException("Illegal energy value (" + energy + ") for " + sys);
        }

        mSystems[sys.ordinal()] = energy;
    }

    /**
     * Convenience, set energy as a percentage [0, 300]
     */
    public void setSystemEnergyAsPercent(ShipSystem sys, float energyPercentage) {
        setSystemEnergy(sys, energyPercentage / Artemis.MAX_ENERGY_ALLOCATION_PERCENT);
    }

    /**
     * The heat level for a system.
     * Unspecified: -1
     */
    public float getSystemHeat(ShipSystem sys) {
    	return mHeat[sys.ordinal()];
    }

    public void setSystemHeat(ShipSystem sys, float heat) {
    	if (heat < 0f || heat > 1f) {
            throw new IllegalArgumentException("Illegal heat value (" + heat + ") for " + sys);
    	}

    	mHeat[sys.ordinal()] = heat;
    }

    /**
     * Whether the shields are up or not.
     */
    public BoolState getShieldsState() {
        return mShields;
    }

    public void setShields(boolean newState) {
        mShields = BoolState.from(newState);
    }

    /**
     * Current alert status.
     */
    public AlertStatus getAlertStatus() {
        return mAlertStatus;
    }

    public void setAlertStatus(AlertStatus alertStatus) {
        mAlertStatus = alertStatus;
    }

    /**
     * Get the ID of the base at which we're docking. Note that this property is
     * only updated in a packet when the docking process commences; undocking
     * does not update this property. However, if an existing ArtemisPlayer
     * object is docked, is updated by another one, and the update has the ship
     * engaging impulse or warp drive, this property will be set to 0 to
     * indicate that the ship has undocked.
     * Unspecified: -1
     */
    public int getDockingBase() {
        return mDockingBase;
    }

    public void setDockingBase(int baseId) {
        mDockingBase = baseId;
    }

    /**
     * The number of torpedoes of the given type in the ship's stores.
     * Unspecified: -1
     */
    public int getTorpedoCount(OrdnanceType type) {
        return mTorpedos[type.ordinal()];
    }

    public void setTorpedoCount(int torpType, int count) {
        mTorpedos[torpType] = count;
    }

    /**
     * Whether or not the ship's impulse drive is in reverse.
     */
    public BoolState getReverseState() {
        return mReverse;
    }

    public void setReverse(BoolState reverse) {
        mReverse = reverse;
    }

    /**
     * The loading state of the given tube.
     * Unspecified: null
     */
    public TubeState getTubeState(int tubeIndex) {
    	return mTubes[tubeIndex].state;
    }

    public void setTubeState(int tubeIndex, TubeState state) {
    	mTubes[tubeIndex].state = state;
    }

    /**
     * Returns a value indicating the contents of the tube. If the tube's state
     * is TubeState.UNLOADED, 0 means that the tube is empty. In any other
     * state, the number will be the ordinal value of the OrdnanceType in the
     * tube.
     * Unspecified: any negative number
     */
    public byte getTubeContentsValue(int tubeIndex) {
    	return mTubes[tubeIndex].contents;
    }

    public void setTubeContentsValue(int tubeIndex, byte value) {
    	mTubes[tubeIndex].contents = value;
    }

    /**
     * Returns an OrdnanceType value indicating the contents of the tube. This
     * method will return null if the tube contents are unspecified, or if the
     * tube is empty.
     */
    public OrdnanceType getTubeContents(int tubeIndex) {
    	Tube tube = mTubes[tubeIndex];

    	if (tube.state == null || tube.contents < 0) {
    		return null;
    	}

    	return tube.state == TubeState.UNLOADED ? null : OrdnanceType.values()[tube.contents];
    }

    /**
     * Sets the contents of the given tube. The tube state must be set to a
     * value other than TubeState.UNLOADED before invoking this method.
     */
    public void setTubeContents(int tubeIndex, OrdnanceType type) {
    	Tube tube = mTubes[tubeIndex];

    	if (tube.state == null) {
    		throw new IllegalStateException("Tube state not set"); 
    	}

    	if (tube.state == TubeState.UNLOADED) {
    		if (type != null) {
    			throw new IllegalArgumentException(
    					"Unloaded tubes cannot contain ordnance"
    			);
    		}

    		tube.contents = 0;
    	} else {
    		if (type == null) {
    			throw new IllegalArgumentException(
    					"No OrdnanceType specified for " + tube.state + " tube state"
    			);
    		}

    		tube.contents = (byte) type.ordinal();
    	}
    }

    /**
     * Returns the number of seconds until the current (un)load operation is
     * complete.
     * Unspecified: -1
     */
    public float getTubeCountdown(int tubeIndex) {
        return mTubes[tubeIndex].secondsLeft;
    }

    public void setTubeCountdown(int tubeIndex, float seconds) {
    	mTubes[tubeIndex].secondsLeft = seconds;
    }

    /**
     * If this is a regular player ship, this value is the amount of coolant in
     * the ship's reserves. If it's a fighter, it's the number of missiles it
     * has. This property exists in order to support the protocol, which uses
     * the same bit to represent both properties, since a packet may not contain
     * the information required to know whether this is a regular player ship or
     * a fighter. For ships that have been tracked by the SystemManager, you can
     * use the separate getAvailableCoolant() and getMissiles() methods.
     * Unspecified: -1
     */
    public byte getAvailableCoolantOrMissiles() {
        return mAvailableCoolantOrMissiles;
    }
    
    public void setAvailableCoolantOrMissiles(byte availableCoolantOrMissiles) {
        mAvailableCoolantOrMissiles = availableCoolantOrMissiles;
    }

    /**
     * Returns the amount of coolant in the ship's reserves, if this is a
     * regular player ship, or 0 if this is a fighter. If the type of ship is
     * unknown, an IllegalStateException will be thrown. It is advisable to only
     * use this method for objects managed by the SystemManager.
     */
    public byte getAvailableCoolant(Context ctx) {
    	Vessel vessel = getVessel(ctx);

    	if (vessel == null) {
    		throw new IllegalStateException("Vessel type unknown");
    	}

    	return vessel.is(VesselAttribute.FIGHTER) ? 0 : mAvailableCoolantOrMissiles;
    }

    /**
     * Sets the amount of available coolant in the ship's reserves, if this is a
     * regular player ship. If this ship is a figher, an
     * UnsupportedOperationException will be thrown. If the ship's type is
     * unknown, an IllegalStateException will be thrown. It is advisable to only
     * use this method for objects managed by the SystemManager.
     */
    public void setAvailableCoolant(Context ctx, byte availableCoolant) {
    	Vessel vessel = getVessel(ctx);

    	if (vessel == null) {
    		throw new IllegalStateException("Vessel type unknown");
    	}

    	if (vessel.is(VesselAttribute.FIGHTER)) {
    		throw new UnsupportedOperationException("Fighters don't have coolant");
    	}

    	mAvailableCoolantOrMissiles = availableCoolant;
    }

    /**
     * Returns the number of missiles on board the fighter, or 0 if this is not
     * a fighter. If the type of ship is unknown, an IllegalStateException will
     * be thrown. It is advisable to only use this method for objects managed by
     * the SystemManager.
     */
    public byte getMissiles(Context ctx) {
    	Vessel vessel = getVessel(ctx);

    	if (vessel == null) {
    		throw new IllegalStateException("Vessel type unknown");
    	}

    	return vessel.is(VesselAttribute.FIGHTER) ? mAvailableCoolantOrMissiles : 0;
    }

    /**
     * Sets the number of missiles on board the figher. If this ship is not a
     * figher, an UnsupportedOperationException will be thrown. If the ship's
     * type is unknown, an IllegalStateException will be thrown. It is advisable
     * to only use this method for objects managed by the SystemManager.
     */
    public void setMissiles(Context ctx, byte missiles) {
    	Vessel vessel = getVessel(ctx);

    	if (vessel == null) {
    		throw new IllegalStateException("Vessel type unknown");
    	}

    	if (!vessel.is(VesselAttribute.FIGHTER)) {
    		throw new UnsupportedOperationException("Only fighters have missiles");
    	}

    	mAvailableCoolantOrMissiles = missiles;
    }

    /**
     * The view for the main screen.
     * Unspecified: null
     */
    public MainScreenView getMainScreen() {
        return mMainScreen;
    }

    public void setMainScreen(MainScreenView screen) {
        mMainScreen = screen;
    }

    /**
     * The frequency at which beam weapons are to be tuned.
     * Unspecified: null
     */
    public BeamFrequency getBeamFrequency() {
    	return mBeamFreq;
    }

    public void setBeamFrequency(BeamFrequency beamFreq) {
    	mBeamFreq = beamFreq;
	}

    /**
     * The type of drive system the ship has.
     * Unspecified: null
     */
    public DriveType getDriveType() {
        return mDriveType;
    }

    public void setDriveType(DriveType driveType) {
        mDriveType = driveType;
    }

    /**
     * Returns the current targeting mode (auto vs. manual beams).
     * Unspecified: null
     */
    public TargetingMode getTargetingMode() {
    	return mTargetingMode;
    }

    public void setTargetingMode(TargetingMode targetingMode) {
    	mTargetingMode = targetingMode;
    }

    /**
     * The ID of the object targeted by the science officer. If the target is
     * cleared, this method returns 1.
     * Unspecified: -1
     */
    public int getScienceTarget() {
        return mScienceTarget;
    }

    public void setScienceTarget(int scienceTarget) {
        mScienceTarget = scienceTarget;
    }

    /**
     * The progress of the current scan, as a value between 0 and 1.
     * Unspecified: -1
     */
    public float getScanProgress() {
        return mScanProgress;
    }

    public void setScanProgress(float scanProgress) {
        mScanProgress = scanProgress;
    }
    
    /**
     * The ID of the object targeted by the captain officer. If the target is
     * cleared, this method returns 1.
     * Unspecified: -1
     */
    public int getCaptainTarget() {
        return mCaptainTarget;
    }

    public void setCaptainTarget(int captainTarget) {
        mCaptainTarget = captainTarget;
    }

    /**
     * The ID of the object targeted by the weapons officer. If the target is
     * cleared, this method returns 1.
     * Unspecified: -1
     */
    public int getWeaponsTarget() {
    	return mWeaponsTarget;
    }

    public void setWeaponsTarget(int weaponsTarget) {
    	mWeaponsTarget = weaponsTarget;
    }

    /**
     * The ID of the object being scanned. Note that this is distinct from the
     * science target: science can start a scan then change targets, but the
     * scan continues on the original target as long as science has not
     * initiated a new scan.
     * Unspecified: -1
     */
    public int getScanObjectId() {
        return mScanningId;
    }

    public void setScanObjectId(int scanningId) {
        mScanningId = scanningId;
    }

    /**
     * Warp factor, between 0 (not at warp) and Artemis.MAX_WARP.
     * Unspecified: -1
     */
    public byte getWarp() {
    	return mWarp;
    }

    public void setWarp(byte warp) {
		mWarp = warp;
	}

    /**
     * Returns whether the indicated upgrade is active.
     * Unspecified: UNKNOWN
     */
    public BoolState isUpgradeActive(Upgrade upgrade) {
    	assertUpgradeCanBeActivated(upgrade);
    	return mUpgrades.get(upgrade).active;
    }

    public void setUpgradeActive(Upgrade upgrade, BoolState active) {
    	assertUpgradeCanBeActivated(upgrade);
    	mUpgrades.get(upgrade).active = active;
    }

    /**
     * Returns the number of upgrades of the indicated type stored on the ship.
     * Unspecified: -1
     */
    public byte getUpgradeCount(Upgrade upgrade) {
    	assertUpgradeCanBeActivated(upgrade);
    	return mUpgrades.get(upgrade).count;
    }

    public void setUpgradeCount(Upgrade upgrade, byte count) {
    	assertUpgradeCanBeActivated(upgrade);
    	mUpgrades.get(upgrade).count = count;
    }

    /**
     * Returns the number of seconds remaining until the given Upgrade expires.
     * The return value will be undefined if isUpgradeActive() returns FALSE
     * for this Upgrade.
     * Unspecified: -1
     */
    public int getUpgradeSecondsLeft(Upgrade upgrade) {
    	assertUpgradeCanBeActivated(upgrade);
    	return mUpgrades.get(upgrade).secondsLeft;
    }

    public void setUpgradeSecondsLeft(Upgrade upgrade, int secondsLeft) {
    	assertUpgradeCanBeActivated(upgrade);
    	mUpgrades.get(upgrade).secondsLeft = secondsLeft;
    }

    /**
     * Returns this ship's accent color as an ARGB value.
     * Unspecified: -1
     */
    public int getAccentColor() {
    	return mAccentColor;
    }

    public void setAccentColor(int accentColor) {
    	mAccentColor = accentColor;
    }

    /**
     * Returns the ID of the capital ship with which this ship can dock. Only applies to fighters.
     * Unspecified: -1
     */
    public int getCapitalShipId() {
    	return mCapitalShipId;
    }

    public void setCapitalShipId(int capitalShipId) {
    	mCapitalShipId = capitalShipId;
    }

    @Override
    public void updateFrom(ArtemisObject obj, Context ctx) {
        super.updateFrom(obj, ctx);
        
        // it should be!
        if (obj instanceof ArtemisPlayer) {
            ArtemisPlayer plr = (ArtemisPlayer) obj;

            if (mShipNumber == -1) {
                mShipNumber = plr.mShipNumber;
            }
            
            if (plr.mTargetingMode != null) {
            	mTargetingMode = plr.mTargetingMode;
            }

            if (plr.mWeaponsTarget != -1) {
                mWeaponsTarget = plr.mWeaponsTarget;
            }

            if (plr.mWarp != -1) {
            	mWarp = plr.mWarp;
            }

            if (plr.mDockingBase != -1) {
                mDockingBase = plr.mDockingBase;
            } else if (plr.getImpulse() != -1 || plr.mWarp != -1) {
            	mDockingBase = 0;
            }

            if (plr.mBeamFreq != null) {
            	mBeamFreq = plr.mBeamFreq;
            }

            if (plr.mAlertStatus != null) {
            	mAlertStatus = plr.mAlertStatus;
            }

            if (BoolState.isKnown(plr.mShields)) {
                mShields = plr.mShields;
            }

            if (plr.mEnergy != -1) {
                mEnergy = plr.mEnergy;
            }
            
            if (plr.mAvailableCoolantOrMissiles != -1) {
                mAvailableCoolantOrMissiles = plr.mAvailableCoolantOrMissiles;
            }
            
            if (plr.mDriveType != null) {
                mDriveType = plr.mDriveType;
            }
            
            for (int i = 0; i < Artemis.SYSTEM_COUNT; i++) {
                if (plr.mHeat[i] != -1) {
                    mHeat[i] = plr.mHeat[i];
                }
                
                if (plr.mSystems[i] != -1) {
                    mSystems[i] = plr.mSystems[i];
                }
                
                if (plr.mCoolant[i] != -1) {
                    mCoolant[i] = plr.mCoolant[i];
                }
            }

            for (int i=0; i < OrdnanceType.COUNT; i++) {
                if (plr.mTorpedos[i] != -1) {
                    mTorpedos[i] = plr.mTorpedos[i];
                }
            }

            for (int i = 0; i < Artemis.MAX_TUBES; i++) {
            	Tube plrTube = plr.mTubes[i];
            	Tube tube = mTubes[i];

            	if (plrTube.secondsLeft >= 0) {
                	tube.secondsLeft = plrTube.secondsLeft < 0.05f ? 0 : plrTube.secondsLeft;
                }

                if (plrTube.state != null) {
                	tube.state = plrTube.state;
                }

                if (plrTube.contents != -1) {
                	tube.contents = plrTube.contents;
                } else if (plrTube.state == TubeState.UNLOADED) {
                	tube.contents = 0;
                }
            }

            if (plr.mMainScreen != null) {
                mMainScreen = plr.mMainScreen;
            }
            
            if (BoolState.isKnown(plr.mReverse)) {
                mReverse = plr.mReverse;
            }
            
            if (plr.mScienceTarget != -1) {
                mScienceTarget = plr.mScienceTarget;
            }

            if (plr.mScanProgress != -1) {
                mScanProgress = plr.mScanProgress;
            }

            if (plr.mCaptainTarget != -1) {
                mCaptainTarget = plr.mCaptainTarget;
            }

            if (plr.mScanningId != -1) {
                mScanningId = plr.mScanningId;
            }

            for (Upgrade upgrade : Upgrade.activation()) {
            	UpgradeStatus plrUpgradeStatus = plr.mUpgrades.get(upgrade);
            	UpgradeStatus upgradeStatus = mUpgrades.get(upgrade);

            	if (BoolState.isKnown(plrUpgradeStatus.active)) {
            		upgradeStatus.active = plrUpgradeStatus.active;
            	}

            	if (plrUpgradeStatus.count != -1) {
            		upgradeStatus.count = plrUpgradeStatus.count;
            	}

            	if (plrUpgradeStatus.secondsLeft != -1) {
            		upgradeStatus.secondsLeft = plrUpgradeStatus.secondsLeft;
            	}
            }

            if (plr.mAccentColor != -1) {
            	mAccentColor = plr.mAccentColor;
            }

            if (plr.mCapitalShipId != -1) {
            	mCapitalShipId = plr.mCapitalShipId;
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props, boolean includeUnspecified) {
    	super.appendObjectProps(props, includeUnspecified);
    	putProp(props, "Targeting mode", mTargetingMode, includeUnspecified);
    	putProp(props, "Alert status", mAlertStatus, includeUnspecified);
    	putProp(props, "Shield state", mShields, includeUnspecified);
    	putProp(props, "Player ship number", mShipNumber, -1, includeUnspecified);

    	for (ShipSystem system : ShipSystem.values()) {
    		int i = system.ordinal();
    		putProp(props, "System heat: " + system, mHeat[i], -1, includeUnspecified);
    		putProp(props, "System energy: " + system, mSystems[i], -1, includeUnspecified);
    		putProp(props, "System coolant: " + system, mCoolant[i], -1, includeUnspecified);
    	}

    	OrdnanceType[] ordValues = OrdnanceType.values();

    	for (OrdnanceType ordnanceType : ordValues) {
    		int i = ordnanceType.ordinal();
    		putProp(props, "Ordnance count: " + ordnanceType, mTorpedos[i], -1, includeUnspecified);
    	}

    	for (int i = 0; i < Artemis.MAX_TUBES; i++) {
    		Tube tube = mTubes[i];
    		putProp(props, "Tube " + i + " state", tube.state, includeUnspecified);
    		String contentsStr;

    		if (tube.state != null && tube.contents != -1) {
    			if (tube.state == TubeState.UNLOADED) {
    				contentsStr = "EMPTY";
    			} else {
    				contentsStr = ordValues[tube.contents].name();
    			}

    			putProp(props, "Tube " + i + " contents", contentsStr, includeUnspecified);
    		}

    		putProp(props, "Tube " + i + " countdown", tube.secondsLeft, -1, includeUnspecified);
    	}

    	putProp(props, "Energy", mEnergy, -1, includeUnspecified);
    	putProp(props, "Docking base", mDockingBase, -1, includeUnspecified);
    	putProp(props, "Main screen view", mMainScreen, includeUnspecified);
    	putProp(props, "Coolant", mAvailableCoolantOrMissiles, -1, includeUnspecified);
    	putProp(props, "Warp", mWarp, -1, includeUnspecified);
    	putProp(props, "Beam frequency", mBeamFreq, includeUnspecified);
    	putProp(props, "Drive type", mDriveType, includeUnspecified);
    	putProp(props, "Reverse", mReverse, includeUnspecified);
    	putProp(props, "Scan target", mScienceTarget, -1, includeUnspecified);
    	putProp(props, "Scan progress", mScanProgress, -1, includeUnspecified);
    	putProp(props, "Scan object ID", mScanningId, -1, includeUnspecified);
    	putProp(props, "Weapons target", mWeaponsTarget, -1, includeUnspecified);
    	putProp(props, "Captain target", mCaptainTarget, -1, includeUnspecified);

    	for (Upgrade upgrade : Upgrade.activation()) {
    		UpgradeStatus status = mUpgrades.get(upgrade);
        	putProp(props, "Upgrades: " + upgrade + ": active", status.active, includeUnspecified);
        	putProp(props, "Upgrades: " + upgrade + ": count", status.count, -1, includeUnspecified);
        	putProp(props, "Upgrades: " + upgrade + ": time", status.secondsLeft, -1, includeUnspecified);
    	}

    	putProp(props, "Capital ship ID", mCapitalShipId, -1, includeUnspecified);
    	putProp(props, "Accent color", mAccentColor, -1, includeUnspecified);
    }

    /**
     * Throws an IllegalArgumentException if the given Upgrade can't be activated.
     */
    private void assertUpgradeCanBeActivated(Upgrade upgrade) {
    	if (upgrade.getActivationIndex() == null) {
    		throw new IllegalArgumentException(upgrade + " upgrades can't be stored on the ship");
    	}
    }
}