package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.TestContext;
import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;
import com.walkertribe.ian.vesseldata.MutableFaction;
import com.walkertribe.ian.vesseldata.MutableVessel;
import com.walkertribe.ian.vesseldata.MutableVesselData;
import com.walkertribe.ian.vesseldata.VesselData;

public class SetShipSettingsPacketTest extends AbstractPacketTester<SetShipSettingsPacket> {
	private static final VesselData VESSEL_DATA;

	static {
		VESSEL_DATA = buildContext().getVesselData();
	}

	@Test
	public void test() {
		execute("core/setup/SetShipSettingsPacket.txt", ConnectionType.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		test(
				new SetShipSettingsPacket(DriveType.WARP, 1000, 0.0f, "Artemis"),
				new SetShipSettingsPacket(DriveType.JUMP, 1001, 0.875f, "Diana")
		);
		test(
				new SetShipSettingsPacket(DriveType.WARP, VESSEL_DATA.getVessel(1000), 0.0f, "Artemis"),
				new SetShipSettingsPacket(DriveType.JUMP, VESSEL_DATA.getVessel(1001), 0.875f, "Diana")
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullDriveType() {
		new SetShipSettingsPacket(null, 0, 0.0f, "Artemis");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullVessel() {
		new SetShipSettingsPacket(DriveType.WARP, null, 0.0f, "Artemis");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNonPlayerVessel() {
		new SetShipSettingsPacket(DriveType.WARP, VESSEL_DATA.getVessel(2000), 0.0f, "Artemis");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructColorTooLow() {
		new SetShipSettingsPacket(DriveType.WARP, 0, -0.0001f, "Artemis");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructColorTooHigh() {
		new SetShipSettingsPacket(DriveType.WARP, 0, 1.0f, "Artemis");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullName() {
		new SetShipSettingsPacket(DriveType.WARP, 0, 0.0f, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructBlankName() {
		new SetShipSettingsPacket(DriveType.WARP, 0, 0.0f, "");
	}

	@Override
	protected void testPackets(List<SetShipSettingsPacket> packets) {
		test(packets.get(0), packets.get(1));
	}

	private void test(SetShipSettingsPacket pkt0, SetShipSettingsPacket pkt1) {
		Ship ship = pkt0.getShip();
		Assert.assertEquals(DriveType.WARP, ship.getDrive());
		Assert.assertEquals(1000, ship.getShipType());
		Assert.assertEquals(0.0f, ship.getAccentColor(), EPSILON);
		TestUtil.assertToStringEquals("Artemis", ship.getName());
		ship = pkt1.getShip();
		Assert.assertEquals(DriveType.JUMP, ship.getDrive());
		Assert.assertEquals(1001, ship.getShipType());
		Assert.assertEquals(0.875f, ship.getAccentColor(), EPSILON);
		TestUtil.assertToStringEquals("Diana", ship.getName());
	}

	private static Context buildContext() {
		TestContext ctx = new TestContext();
		MutableVesselData vesselData = new MutableVesselData(ctx);
		ctx.setVesselData(vesselData);
		vesselData.putFaction(new MutableFaction(0, "TSN", "player"));
		vesselData.putFaction(new MutableFaction(1, "Skaraan", "enemy loner hasspecials"));
		vesselData.putVessel(new MutableVessel(ctx, 1000, 0, "Cruiser", "player small"));
		vesselData.putVessel(new MutableVessel(ctx, 1001, 0, "Cruiser", "player small"));
		vesselData.putVessel(new MutableVessel(ctx, 2000, 1, "Defiler", "small"));
		return ctx;
	}
}
