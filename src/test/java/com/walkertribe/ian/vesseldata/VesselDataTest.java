package com.walkertribe.ian.vesseldata;

import static org.junit.Assert.*;

import com.walkertribe.ian.TestContext;
import com.walkertribe.ian.enums.FactionAttribute;

import org.junit.BeforeClass;
import org.junit.Test;

public class VesselDataTest {
    private static final String[] FACTIONS = {
            "TSN:PLAYER",
            "Terran:FRIENDLY",
            "Kralien:ENEMY STANDARD",
            "Arvonian:ENEMY SUPPORT WHALELOVER",
            "Torgoth:ENEMY SUPPORT WHALEHATER",
            "Skaraan:ENEMY LONER HASSPECIALS",
            "BioMech:ENEMY LONER BIOMECH",
            "Ximni:PLAYER JUMPMASTER"
    };
    private static final String[] PLAYER_VESSELS = {
            "0:0:Light Cruiser:PLAYER:80:80",
            "1:0:Scout:PLAYER:60:60",
            "2:0:Battleship:PLAYER:250:150",
            "3:0:Missile Cruiser:PLAYER:110:80",
            "4:0:Dreadnought:PLAYER CARRIER:200:200",
            "5:0:Carrier:PLAYER CARRIER:100:100",
            "6:0:Mine Layer:PLAYER:150:150",
            "7:0:Juggernaut:PLAYER CARRIER:300:300",
            "8:7:Light Cruiser:PLAYER:80:80",
            "9:7:Scout:PLAYER:60:60",
            "10:7:Missile Cruiser:PLAYER:110:80",
            "11:7:Battleship:PLAYER:250:150",
            "12:7:Carrier:PLAYER CARRIER:80:80",
            "13:7:Dreadnought:PLAYER CARRIER:200:200",
            "100:0:Fighter XA:PLAYER FIGHTER:15:15",
            "120:7:Zim Fighter:PLAYER FIGHTER:15:15"
    };

    private static VesselData vesselData;

	@BeforeClass
	public static void beforeClass() {
	    TestContext ctx = new TestContext();
	    MutableVesselData mvd = new MutableVesselData(ctx);
	    ctx.setVesselData(mvd);
	    int i = 0;

	    for (String factionInfo : FACTIONS) {
	        String[] parts = factionInfo.split(":");
	        mvd.putFaction(new MutableFaction(i++, parts[0], parts[1]));
	    }

	    for (String vesselInfo : PLAYER_VESSELS) {
	        String[] parts = vesselInfo.split(":");
	        MutableVessel vessel = new MutableVessel(
                    ctx,
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    parts[2],
                    parts[3]
	        );
            vessel.setForeShields(Integer.parseInt(parts[4]));
            vessel.setAftShields(Integer.parseInt(parts[5]));
            mvd.putVessel(vessel);
	    }

	    vesselData = mvd;
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
			int foreShield, int aftShield, String... attrs) {
		assertEquals(name, vessel.getName());
		assertEquals(side, vessel.getSide());
		assertEquals(foreShield, vessel.getForeShields());
		assertEquals(aftShield, vessel.getAftShields());
		assertEquals(attrs.length, vessel.getAttributes().length);
		assertTrue(vessel.is(attrs));
	}
}