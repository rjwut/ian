package com.walkertribe.ian.vesseldata;

import static org.junit.Assert.*;

import java.io.File;

import com.walkertribe.ian.enums.FactionAttribute;
import com.walkertribe.ian.enums.VesselAttribute;

import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class VesselDataTest {
	private static final String PROPERTY_INSTALL_PATH = "artemisInstallPath";
	private static VesselData vesselData;

	@BeforeClass
	public static void beforeClass() {
		String installPath = System.getProperty(PROPERTY_INSTALL_PATH);

		if (installPath != null) {
			VesselData.setArtemisInstallPath(new File(installPath));
			vesselData = VesselData.get();
			VesselData.preloadInternals();
			VesselData.preloadModels();
		}
	}

	@Before
	public void before() {
		Assume.assumeTrue(
				"No -D" + PROPERTY_INSTALL_PATH + " specified in VM arguments",
				vesselData != null
		);
	}

	@Test
	public void testFactions() {
		testFaction(vesselData.getFaction(0), "TSN", FactionAttribute.PLAYER);
		testFaction(vesselData.getFaction(1), "Terran", FactionAttribute.FRIENDLY);
		testFaction(vesselData.getFaction(2), "Kralien", FactionAttribute.ENEMY,
				FactionAttribute.STANDARD);
		testFaction(vesselData.getFaction(3), "Arvonian", FactionAttribute.ENEMY,
				FactionAttribute.SUPPORT, FactionAttribute.WHALELOVER);
		testFaction(vesselData.getFaction(4), "Torgoth", FactionAttribute.ENEMY,
				FactionAttribute.SUPPORT, FactionAttribute.WHALEHATER);
		testFaction(vesselData.getFaction(5), "Skaraan", FactionAttribute.ENEMY,
				FactionAttribute.LONER, FactionAttribute.ELITE);
		testFaction(vesselData.getFaction(6), "BioMech", FactionAttribute.ENEMY,
				FactionAttribute.LONER, FactionAttribute.BIOMECH);
	}

	@Test
	public void testVessels() {
		testVessel(vesselData.getVessel(0), "Light Cruiser", 0, 80, 80,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(1), "Scout", 0, 60, 60,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(2), "Battleship", 0, 250, 150,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(3), "Missile Cruiser", 0, 110, 80,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(4), "Dreadnought", 0, 200, 200,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(5), "Carrier", 0, 100, 100,
				VesselAttribute.PLAYER, VesselAttribute.CARRIER);
		testVessel(vesselData.getVessel(100), "Fighter XA", 0, 140, 0,
				VesselAttribute.PLAYER, VesselAttribute.FIGHTER);
	}

	private static void testFaction(Faction faction, String name,
			FactionAttribute... attrs) {
		assertEquals(name, faction.getName());
		assertEquals(attrs.length, faction.getAttributes().length);
		assertTrue(faction.is(attrs));
	}

	private static void testVessel(Vessel vessel, String name, int side,
			int foreShield, int aftShield, VesselAttribute... attrs) {
		assertEquals(name, vessel.getName());
		assertEquals(0, vessel.getSide());
		assertEquals(foreShield, vessel.getForeShields());
		assertEquals(aftShield, vessel.getAftShields());
		assertEquals(attrs.length, vessel.getAttributes().length);
		assertTrue(vessel.is(attrs));
	}
}