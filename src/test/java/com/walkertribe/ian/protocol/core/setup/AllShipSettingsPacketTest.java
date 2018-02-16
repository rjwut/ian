package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.protocol.core.setup.Ship;
import com.walkertribe.ian.util.TestUtil;
import com.walkertribe.ian.world.Artemis;

public class AllShipSettingsPacketTest extends AbstractPacketTester<AllShipSettingsPacket> {
	@Test
	public void test() {
		execute("core/setup/AllShipSettingsPacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		Ship[] ships = buildShips(Artemis.SHIP_COUNT, true);
		AllShipSettingsPacket pkt = new AllShipSettingsPacket(ships);

		for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
			Assert.assertEquals(ships[i], pkt.getShip(i + 1));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullArray() {
		new AllShipSettingsPacket(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructArrayContainsNull() {
		Ship[] ships = buildShips(Artemis.SHIP_COUNT, false);
		ships[Artemis.SHIP_COUNT - 1] = null;
		new AllShipSettingsPacket(ships);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructArrayTooShort() {
		new AllShipSettingsPacket(buildShips(Artemis.SHIP_COUNT - 1, false));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructArrayTooLong() {
		new AllShipSettingsPacket(buildShips(Artemis.SHIP_COUNT + 1, false));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructShipNullName() {
		new Ship((String) null, 0, 0.0f, DriveType.WARP);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructShipEmptyName() {
		new Ship("", 0, 0.0f, DriveType.WARP);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructShipAccentColorTooLow() {
		new Ship("Artemis", 0, -0.001f, DriveType.WARP);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructShipAccentColorTooHigh() {
		new Ship("Artemis", 0, 1.0f, DriveType.WARP);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructShipNullDrive() {
		new Ship("Artemis", 0, 0.0f, null);
	}

	@Override
	protected void testPackets(List<AllShipSettingsPacket> packets) {
		AllShipSettingsPacket pkt = packets.get(0);
		float colorInc = 1.0f / Artemis.SHIP_COUNT;

		for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
			Ship ship = pkt.getShip(i + 1);
			TestUtil.assertToStringEquals("Ship" + i, ship.getName());
			Assert.assertEquals(i, ship.getShipType());
			Assert.assertEquals(DriveType.values()[i % 2], ship.getDrive());
			Assert.assertEquals(colorInc * i, ship.getAccentColor(), EPSILON);
		}
	}

	private Ship[] buildShips(int shipCount, boolean doAsserts) {
		Ship[] ships = new Ship[shipCount];
		float colorInc = 1.0f / shipCount;

		for (int i = 0; i < shipCount; i++) {
			String name = "Ship" + i;
			float color = i * colorInc;
			DriveType drive = DriveType.values()[i % 2];
			Ship ship = new Ship(name, i, color, drive);

			if (doAsserts) {
				Assert.assertEquals(name, ship.getName());
				Assert.assertEquals(i, ship.getShipType());
				Assert.assertEquals(color, ship.getAccentColor(), EPSILON);
				Assert.assertEquals(drive, ship.getDrive());
			}

			ships[i] = ship;
		}

		return ships;
	}
}
