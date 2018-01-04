package com.walkertribe.ian.vesseldata;

import static org.junit.Assert.*;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.FactionAttribute;
import com.walkertribe.ian.enums.VesselAttribute;
import com.walkertribe.ian.util.TestUtil;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class VesselDataTest {
	private static VesselData vesselData;

	@BeforeClass
	public static void beforeClass() {
		Context ctx = TestUtil.getContext();

		if (ctx != null) {
			vesselData = ctx.getVesselData();
			vesselData.preloadInternals();
			vesselData.preloadModels();
		}
	}

	@Before
	public void before() {
		TestUtil.assumeContext();
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
				FactionAttribute.LONER, FactionAttribute.HASSPECIALS);
		testFaction(vesselData.getFaction(6), "BioMech", FactionAttribute.ENEMY,
				FactionAttribute.LONER, FactionAttribute.BIOMECH);
		testFaction(vesselData.getFaction(7), "Ximni", FactionAttribute.PLAYER,
				FactionAttribute.JUMPMASTER);
	}

	@Test
	public void testPlayerVessels() {
		testVessel(vesselData.getVessel(0), "Light Cruiser", 0, 80, 80,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(1), "Scout", 0, 60, 60,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(2), "Battleship", 0, 250, 150,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(3), "Missile Cruiser", 0, 110, 80,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(4), "Dreadnought", 0, 200, 200,
				VesselAttribute.PLAYER, VesselAttribute.CARRIER);
		testVessel(vesselData.getVessel(5), "Carrier", 0, 100, 100,
				VesselAttribute.PLAYER, VesselAttribute.CARRIER);
		testVessel(vesselData.getVessel(6), "Mine Layer", 0, 150, 150,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(7), "Juggernaut", 0, 300, 300,
				VesselAttribute.PLAYER, VesselAttribute.CARRIER);
		testVessel(vesselData.getVessel(8), "Light Cruiser", 7, 80, 80,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(9), "Scout", 7, 60, 60,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(10), "Missile Cruiser", 7, 110, 80,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(11), "Battleship", 7, 250, 150,
				VesselAttribute.PLAYER);
		testVessel(vesselData.getVessel(12), "Carrier", 7, 80, 80,
				VesselAttribute.PLAYER, VesselAttribute.CARRIER);
		testVessel(vesselData.getVessel(13), "Dreadnought", 7, 200, 200,
				VesselAttribute.PLAYER, VesselAttribute.CARRIER);
		testVessel(vesselData.getVessel(100), "Fighter XA", 0, 15, 15,
				VesselAttribute.PLAYER, VesselAttribute.FIGHTER);
		testVessel(vesselData.getVessel(120), "Zim Fighter", 7, 15, 15,
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
		assertEquals(side, vessel.getSide());
		assertEquals(foreShield, vessel.getForeShields());
		assertEquals(aftShield, vessel.getAftShields());
		assertEquals(attrs.length, vessel.getAttributes().length);
		assertTrue(vessel.is(attrs));
	}
}