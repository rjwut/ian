package com.walkertribe.ian.world;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.TestContext;
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
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.TestUtil;
import com.walkertribe.ian.vesseldata.MutableFaction;
import com.walkertribe.ian.vesseldata.MutableVessel;
import com.walkertribe.ian.vesseldata.MutableVesselData;

public class ArtemisPlayerTest {
	public static final TubeState[] WEAP_TUBE_STATE_UNSPECIFIED = new TubeState[Artemis.MAX_TUBES];
	public static final OrdnanceType[] WEAP_TUBE_CONTENTS_UNSPECIFIED = new OrdnanceType[Artemis.MAX_TUBES];
	public static final int[] WEAP_ORD_COUNT = new int[OrdnanceType.COUNT];
	public static final float[] WEAP_TUBE_TIME = new float[Artemis.MAX_TUBES];
	public static final TubeState[] WEAP_TUBE_STATE = new TubeState[Artemis.MAX_TUBES];
	public static final OrdnanceType[] WEAP_TUBE_CONTENTS = new OrdnanceType[Artemis.MAX_TUBES];
	public static final float[] ENG_FLOATS = new float[Artemis.SYSTEM_COUNT];
	public static final byte[] ENG_BYTES = new byte[Artemis.SYSTEM_COUNT];
	public static final Map<Upgrade, BoolState> UPGRADE_ACTIVE_UNSPECIFIED = new HashMap<Upgrade, BoolState>();
	public static final Map<Upgrade, Byte> UPGRADE_COUNT_UNSPECIFIED = new HashMap<Upgrade, Byte>();
	public static final Map<Upgrade, Integer> UPGRADE_SECONDS_LEFT_UNSPECIFIED = new HashMap<Upgrade, Integer>();
	public static final Map<Upgrade, BoolState> UPGRADE_ACTIVE = new HashMap<Upgrade, BoolState>();
	public static final Map<Upgrade, Byte> UPGRADE_COUNT = new HashMap<Upgrade, Byte>();
	public static final Map<Upgrade, Integer> UPGRADE_SECONDS_LEFT = new HashMap<Upgrade, Integer>();

	static {
		float part = 1.0f / Artemis.SYSTEM_COUNT;

		for (byte i = 0; i < Artemis.SYSTEM_COUNT; i++) {
			ENG_FLOATS[i] = i * part;
			ENG_BYTES[i] = i;
		}

		for (int i = 0; i < OrdnanceType.COUNT; i++) {
			WEAP_ORD_COUNT[i] = i;
		}

		for (int i = 0; i < Artemis.MAX_TUBES; i++) {
			TubeState state = TubeState.values()[i % 4];
			WEAP_TUBE_TIME[i] = i;
			WEAP_TUBE_STATE[i] = state;
			WEAP_TUBE_CONTENTS[i] = state != TubeState.UNLOADED ? OrdnanceType.values()[i % OrdnanceType.COUNT] : null;
		}

		int i = 0;

		for (Upgrade upgrade : Upgrade.activation()) {
			UPGRADE_ACTIVE_UNSPECIFIED.put(upgrade, BoolState.UNKNOWN);
			UPGRADE_COUNT_UNSPECIFIED.put(upgrade, (byte) -1);
			UPGRADE_SECONDS_LEFT_UNSPECIFIED.put(upgrade, -1);
			boolean active = i % 2 != 0;
			UPGRADE_ACTIVE.put(upgrade, BoolState.from(active));
			UPGRADE_COUNT.put(upgrade, (byte) i);
			UPGRADE_SECONDS_LEFT.put(upgrade, active ? i : 0);
			i++;
		}
	}

	@Test
	public void testSplit() {
		ArtemisPlayer player = new ArtemisPlayer(47);
		Map<ObjectType, ArtemisPlayer> parts = player.split();
		Assert.assertTrue(parts.isEmpty());
		buildPlayerData(player);
		buildWeapData(player);
		buildEngData(player);
		buildUpgradeData(player);
		assertPopulatedPlayer(player);
		assertPopulatedWeap(player);
		assertPopulatedEng(player);
		assertPopulatedUpgrades(player);
		parts = player.split();
		assertPopulatedPlayer(parts.get(ObjectType.PLAYER_SHIP));
		assertPopulatedWeap(parts.get(ObjectType.WEAPONS_CONSOLE));
		assertPopulatedEng(parts.get(ObjectType.ENGINEERING_CONSOLE));
		assertPopulatedUpgrades(parts.get(ObjectType.UPGRADES));
	}

	@Test
	public void testAppendObjectPropsForTubes() {
		// state = UNLOADED, contents = [unspecified]
		ArtemisPlayer player = new ArtemisPlayer(47);
		player.setTubeState(0, TubeState.UNLOADED);
		SortedMap<String, Object> props = new TreeMap<String, Object>();
		player.appendObjectProps(props);
		Assert.assertNull(props.get("Tube 0 contents"));

		// state = UNLOADED, contents = null
		player = new ArtemisPlayer(47);
		player.setTubeState(0, TubeState.UNLOADED);
		player.setTubeContents(0, null);
		props = new TreeMap<String, Object>();
		player.appendObjectProps(props);
		Assert.assertEquals("EMPTY", props.get("Tube 0 contents"));

		// state = LOADING, contents = HOMING
		player = new ArtemisPlayer(47);
		player.setTubeState(0, TubeState.LOADING);
		player.setTubeContents(0, OrdnanceType.TORPEDO);
		props = new TreeMap<String, Object>();
		player.appendObjectProps(props);
		Assert.assertEquals("TORPEDO", props.get("Tube 0 contents"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWarpTooLow() {
		new ArtemisPlayer(47).setWarp((byte) -2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWarpTooHigh() {
		new ArtemisPlayer(47).setWarp((byte) (Artemis.MAX_WARP + 1));
	}

	@Test
	public void testUndock() {
		// activate impulse while docked
		ArtemisPlayer obj1 = new ArtemisPlayer(47);
		obj1.setDockingBase(1);
		ArtemisPlayer obj2 = new ArtemisPlayer(47);
		obj2.setImpulse(1);
		obj1.updatePlayerFrom(obj2);
		Assert.assertEquals(0, obj1.getDockingBase());

		// activate warp while docked
		obj1 = new ArtemisPlayer(47);
		obj1.setDockingBase(1);
		obj2 = new ArtemisPlayer(47);
		obj2.setWarp((byte) 1);
		obj1.updatePlayerFrom(obj2);
		Assert.assertEquals(0, obj1.getDockingBase());

		// activate both impulse and warp while docked (!)
		obj1 = new ArtemisPlayer(47);
		obj1.setDockingBase(1);
		obj2 = new ArtemisPlayer(47);
		obj2.setImpulse(1);
		obj2.setWarp((byte) 1);
		obj1.updatePlayerFrom(obj2);
		Assert.assertEquals(0, obj1.getDockingBase());
	}

	@Test
	public void testUpdateFrom() {
		ArtemisPlayer obj0 = new ArtemisPlayer(47);
		assertUnpopulatedPlayer(obj0);
		ArtemisPlayer obj1 = new ArtemisPlayer(47);
		buildPlayerData(obj1);
		assertPopulatedPlayer(obj1);
		obj1.updateFrom(obj0);
		assertPopulatedPlayer(obj1);
		obj0.updateFrom(obj1);
		assertPopulatedPlayer(obj0);
		obj0.updateFrom(new ArtemisCreature(48));
	}

	private static void buildPlayerData(ArtemisPlayer player) {
		player.setTargetingMode(TargetingMode.AUTO);
		player.setAlertStatus(AlertStatus.NORMAL);
		player.setShields(BoolState.FALSE);
		player.setShipIndex((byte) 0);
		player.setEnergy(1000f);
		player.setDockingBase(0);
		player.setMainScreen(MainScreenView.FORE);
		player.setAvailableCoolantOrMissiles((byte) 8);
		player.setWeaponsTarget(1);
		player.setWarp((byte) 0);
		player.setBeamFrequency(BeamFrequency.A);
		player.setDriveType(DriveType.WARP);
		player.setReverse(BoolState.FALSE);
		player.setScienceTarget(1);
		player.setScanProgress(0f);
		player.setCaptainTarget(1);
		player.setScanObjectId(1);
		player.setCapitalShipId(1000);
		player.setAccentColor(0f);
	}

	public static void assertUnpopulatedPlayer(ArtemisPlayer player) {
		Assert.assertNull(player.getTargetingMode());
		Assert.assertNull(player.getAlertStatus());
		Assert.assertTrue(!BoolState.isKnown(player.getShieldsState()));
		Assert.assertEquals(Byte.MIN_VALUE, player.getShipIndex());
		Assert.assertTrue(Float.isNaN(player.getEnergy()));
		Assert.assertEquals(-1, player.getDockingBase());
		Assert.assertNull(player.getMainScreen());
		Assert.assertEquals(-1, player.getAvailableCoolantOrMissiles());
		Assert.assertEquals(-1, player.getWeaponsTarget());
		Assert.assertEquals(-1, player.getWarp());
		Assert.assertNull(player.getBeamFrequency());
		Assert.assertNull(player.getDriveType());
		Assert.assertTrue(!BoolState.isKnown(player.getReverseState()));
		Assert.assertEquals(-1, player.getScienceTarget());
		Assert.assertTrue(Float.isNaN(player.getScanProgress()));
		Assert.assertEquals(-1, player.getCaptainTarget());
		Assert.assertEquals(-1, player.getScanObjectId());
		Assert.assertEquals(-1, player.getCapitalShipId());
		Assert.assertTrue(Float.isNaN(player.getAccentColor()));
	}

	public static void assertPopulatedPlayer(ArtemisPlayer player) {
		assertPlayer(player, TargetingMode.AUTO, AlertStatus.NORMAL, BoolState.FALSE, 0, 1000f, 0, MainScreenView.FORE,
				(byte) 8, 1, (byte) 0, BeamFrequency.A, DriveType.WARP, BoolState.FALSE, 1, 0f, 1, 1, 1000, 0f);
	}

	public static void assertPlayer(ArtemisPlayer player, TargetingMode targetingMode, AlertStatus alertStatus,
			BoolState shields, int shipIndex, float energy, int dockingBase, MainScreenView mainScreen,
			byte coolantOrMissiles, int weaponsTarget, byte warp, BeamFrequency beamFreq, DriveType drive,
			BoolState reverse, int scienceTarget, float scanProgress, int captainTarget, int scanningId,
			int capitalShipId, float accentColor) {
		Assert.assertEquals(targetingMode, player.getTargetingMode());
		Assert.assertEquals(alertStatus, player.getAlertStatus());
		Assert.assertEquals(shields, player.getShieldsState());
		Assert.assertEquals(shipIndex, player.getShipIndex());
		Assert.assertEquals(energy, player.getEnergy(), TestUtil.EPSILON);
		Assert.assertEquals(dockingBase, player.getDockingBase());
		Assert.assertEquals(mainScreen, player.getMainScreen());
		Assert.assertEquals(coolantOrMissiles, player.getAvailableCoolantOrMissiles());
		Assert.assertEquals(weaponsTarget, player.getWeaponsTarget());
		Assert.assertEquals(warp, player.getWarp());
		Assert.assertEquals(beamFreq, player.getBeamFrequency());
		Assert.assertEquals(drive, player.getDriveType());
		Assert.assertEquals(reverse, player.getReverseState());
		Assert.assertEquals(scienceTarget, player.getScienceTarget());
		Assert.assertEquals(scanProgress, player.getScanProgress(), TestUtil.EPSILON);
		Assert.assertEquals(captainTarget, player.getCaptainTarget());
		Assert.assertEquals(scanningId, player.getScanObjectId());
		Assert.assertEquals(capitalShipId, player.getCapitalShipId());
		Assert.assertEquals(accentColor, player.getAccentColor(), TestUtil.EPSILON);
	}

	// WEAP DATA

	@Test(expected = IllegalStateException.class)
	public void testWeapSetContentsBeforeState() {
		new ArtemisPlayer(47).setTubeContents(0, OrdnanceType.TORPEDO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWeapSetUnloadedTubeContents() {
		ArtemisPlayer player = new ArtemisPlayer(47);
		player.setTubeState(0, TubeState.UNLOADED);
		player.setTubeContents(0, OrdnanceType.TORPEDO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWeapSetLoadingTubeEmpty() {
		ArtemisPlayer player = new ArtemisPlayer(47);
		player.setTubeState(0, TubeState.LOADING);
		player.setTubeContents(0, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWeapSetLoadedTubeEmpty() {
		ArtemisPlayer player = new ArtemisPlayer(47);
		player.setTubeState(0, TubeState.LOADED);
		player.setTubeContents(0, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWeapSetUnloadingTubeEmpty() {
		ArtemisPlayer player = new ArtemisPlayer(47);
		player.setTubeState(0, TubeState.UNLOADING);
		player.setTubeContents(0, null);
	}

	@Test
	public void testUpdateFromWeap() {
		ArtemisPlayer obj0 = new ArtemisPlayer(47);
		assertUnpopulatedWeap(obj0);
		ArtemisPlayer obj1 = new ArtemisPlayer(47);
		buildWeapData(obj1);
		assertPopulatedWeap(obj1);
		obj1.updateFrom(obj0);
		assertPopulatedWeap(obj1);
		obj0.updateFrom(obj1);
		assertPopulatedWeap(obj0);
	}

	private static void buildWeapData(ArtemisPlayer player) {
		for (OrdnanceType type : OrdnanceType.values()) {
			player.setTorpedoCount(type, type.ordinal());
		}

		for (int i = 0; i < Artemis.MAX_TUBES; i++) {
			TubeState state = TubeState.values()[i % 4];
			player.setTubeState(i, state);
			player.setTubeContents(i, state != TubeState.UNLOADED ? OrdnanceType.values()[i % OrdnanceType.COUNT] : null);
			player.setTubeCountdown(i, i);
		}
	}

	public static void assertUnpopulatedWeap(ArtemisPlayer player) {
		for (OrdnanceType type : OrdnanceType.values()) {
			Assert.assertEquals(-1, player.getTorpedoCount(type));
		}

		for (int i = 0; i < Artemis.MAX_TUBES; i++) {
			Assert.assertTrue(Float.isNaN(player.getTubeCountdown(i)));
			Assert.assertNull(player.getTubeState(i));
			Assert.assertNull(player.getTubeState(i));
			Assert.assertNull(player.getTubeContents(i));
		}
	}

	public static void assertPopulatedWeap(ArtemisPlayer player) {
		assertWeap(player, WEAP_ORD_COUNT, WEAP_TUBE_TIME, WEAP_TUBE_STATE, WEAP_TUBE_CONTENTS);
	}

	public static void assertWeap(ArtemisPlayer player, int[] ordnanceCounts,
			float[] tubeTimes, TubeState[] tubeStates, OrdnanceType[] tubeContents) {
		for (OrdnanceType type : OrdnanceType.values()) {
			Assert.assertEquals(ordnanceCounts[type.ordinal()], player.getTorpedoCount(type));
		}

		for (int i = 0; i < Artemis.MAX_TUBES; i++) {
			TubeState state = player.getTubeState(i);

			if (state != null && state != TubeState.UNLOADED) {
				Assert.assertEquals(tubeTimes[i], player.getTubeCountdown(i), TestUtil.EPSILON);
			}

			Assert.assertEquals(tubeStates[i], state);
			Assert.assertEquals(tubeStates[i], player.getTubeState(i));
			Assert.assertEquals(tubeContents[i], player.getTubeContents(i));
		}
	}

	// ENG DATA

	@Test(expected = IllegalArgumentException.class)
	public void testEngHeatTooLow() {
		new ArtemisPlayer(47).setSystemHeat(ShipSystem.BEAMS, -0.0001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngHeatTooHigh() {
		new ArtemisPlayer(47).setSystemHeat(ShipSystem.BEAMS, 1.0001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngEnergyTooLow() {
		new ArtemisPlayer(47).setSystemEnergy(ShipSystem.BEAMS, -0.0001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngEnergyTooHigh() {
		new ArtemisPlayer(47).setSystemEnergy(ShipSystem.BEAMS, 1.0001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngCoolantTooLow() {
		new ArtemisPlayer(47).setSystemCoolant(ShipSystem.BEAMS, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngCoolantTooHigh() {
		new ArtemisPlayer(47).setSystemCoolant(ShipSystem.BEAMS, Artemis.MAX_COOLANT_PER_SYSTEM + 1);
	}

	@Test
	public void testUpdateFromEng() {
		ArtemisPlayer obj0 = new ArtemisPlayer(47);
		assertUnpopulatedEng(obj0);
		ArtemisPlayer obj1 = new ArtemisPlayer(47);
		buildEngData(obj1);
		assertPopulatedEng(obj1);
		obj1.updateFrom(obj0);
		assertPopulatedEng(obj1);
		obj0.updateFrom(obj1);
		assertPopulatedEng(obj0);
	}

	private static void buildEngData(ArtemisPlayer player) {
		for (ShipSystem sys : ShipSystem.values()) {
			int index = sys.ordinal();
			player.setSystemHeat(sys, ENG_FLOATS[index]);
			player.setSystemEnergyAsPercent(sys, ENG_FLOATS[index] * Artemis.MAX_ENERGY_ALLOCATION_PERCENT);
			player.setSystemCoolant(sys, ENG_BYTES[index]);
		}
	}

	public static void assertUnpopulatedEng(ArtemisPlayer player) {
		for (ShipSystem system : ShipSystem.values()) {
			Assert.assertTrue(Float.isNaN(player.getSystemHeat(system)));
			Assert.assertTrue(Float.isNaN(player.getSystemEnergy(system)));
			Assert.assertEquals(-1, player.getSystemCoolant(system));
		}
	}

	private static void assertPopulatedEng(ArtemisPlayer player) {
		assertEng(player, ENG_FLOATS, ENG_FLOATS, ENG_BYTES);
	}

	public static void assertEng(ArtemisPlayer player, float[] heat, float[] energy, byte[] coolant) {
		for (ShipSystem system : ShipSystem.values()) {
			int index = system.ordinal();
			Assert.assertEquals(heat[index], player.getSystemHeat(system), TestUtil.EPSILON);
			Assert.assertEquals(energy[index], player.getSystemEnergy(system), TestUtil.EPSILON);
			Assert.assertEquals(coolant[index], player.getSystemCoolant(system));
		}
	}

	// UPGRADE DATA

	@Test(expected = IllegalArgumentException.class)
	public void testisNonActivatableUpgradeActive() {
		new ArtemisPlayer(47).isUpgradeActive(Upgrade.HIDENS_POWER_CELL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetNonActivatableUpgradeActive() {
		new ArtemisPlayer(47).setUpgradeActive(Upgrade.HIDENS_POWER_CELL, BoolState.TRUE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetNonActivatableUpgradeCount() {
		new ArtemisPlayer(47).getUpgradeCount(Upgrade.HIDENS_POWER_CELL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetNonActivatableUpgradeCount() {
		new ArtemisPlayer(47).setUpgradeCount(Upgrade.HIDENS_POWER_CELL, (byte) 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetNonActivatableUpgradeSecondsLeft() {
		new ArtemisPlayer(47).getUpgradeSecondsLeft(Upgrade.HIDENS_POWER_CELL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetNonActivatableUpgradeSecondsLeft() {
		new ArtemisPlayer(47).setUpgradeSecondsLeft(Upgrade.HIDENS_POWER_CELL, 1);
	}

	@Test
	public void testUpdateFromUpgrades() {
		ArtemisPlayer obj0 = new ArtemisPlayer(47);
		assertUnpopulatedUpgrades(obj0);
		ArtemisPlayer obj1 = new ArtemisPlayer(47);
		buildUpgradeData(obj1);
		assertPopulatedUpgrades(obj1);
		obj1.updateFrom(obj0);
		assertPopulatedUpgrades(obj1);
		obj0.updateFrom(obj1);
		assertPopulatedUpgrades(obj0);
	}

	private static void buildUpgradeData(ArtemisPlayer player) {
		int i = 0;

		for (Upgrade upgrade : Upgrade.activation()) {
			boolean active = i % 2 != 0;
			player.setUpgradeActive(upgrade, active);
			player.setUpgradeCount(upgrade, (byte) i);
			player.setUpgradeSecondsLeft(upgrade, active ? i : 0);
			i++;
		}
	}

	public static void assertUnpopulatedUpgrades(ArtemisPlayer player) {
		for (Upgrade upgrade : Upgrade.activation()) {
			Assert.assertTrue(!BoolState.isKnown(player.isUpgradeActive(upgrade)));
			Assert.assertEquals(-1, player.getUpgradeCount(upgrade));
			Assert.assertEquals(-1, player.getUpgradeSecondsLeft(upgrade));
		}
	}

	public static void assertPopulatedUpgrades(ArtemisPlayer player) {
		assertUpgrades(player, UPGRADE_ACTIVE, UPGRADE_COUNT, UPGRADE_SECONDS_LEFT);
	}

	public static void assertUpgrades(ArtemisPlayer player, Map<Upgrade, BoolState> active,
			Map<Upgrade, Byte> count, Map<Upgrade, Integer> secondsLeft) {
		for (Upgrade upgrade : Upgrade.activation()) {
			Assert.assertEquals(active.get(upgrade), player.isUpgradeActive(upgrade));
			Assert.assertEquals(count.get(upgrade).byteValue(), player.getUpgradeCount(upgrade));
			Assert.assertEquals(secondsLeft.get(upgrade).intValue(), player.getUpgradeSecondsLeft(upgrade));
		}
	}


	public static Context buildContext() {
		TestContext ctx = new TestContext();
		MutableVesselData vesselData = new MutableVesselData(ctx);
		ctx.setVesselData(vesselData);
		vesselData.putFaction(new MutableFaction(0, "TSN", "player"));
		vesselData.putFaction(new MutableFaction(1, "Skaraan", "enemy loner hasspecials"));
		vesselData.putVessel(new MutableVessel(ctx, 1000, 0, "Cruiser", "small"));
		vesselData.putVessel(new MutableVessel(ctx, 2000, 1, "Defiler", "small"));
		return ctx;
	}
}
