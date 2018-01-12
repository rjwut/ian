package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class SetShipSettingsPacketTest extends AbstractPacketTester<SetShipSettingsPacket> {
	@Test
	public void test() {
		execute("core/setup/SetShipSettingsPacket.txt", ConnectionType.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		test(
				new SetShipSettingsPacket(DriveType.WARP, 0, 0.0f, "Artemis"),
				new SetShipSettingsPacket(DriveType.JUMP, 47, 0.875f, "Diana")
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullDriveType() {
		new SetShipSettingsPacket(null, 0, 0.0f, "Artemis");
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
		Assert.assertEquals(DriveType.WARP, pkt0.getDrive());
		Assert.assertEquals(0, pkt0.getHullId());
		Assert.assertEquals(0.0f, pkt0.getAccentColor(), EPSILON);
		Assert.assertEquals("Artemis", pkt0.getName());
		Assert.assertEquals(DriveType.JUMP, pkt1.getDrive());
		Assert.assertEquals(47, pkt1.getHullId());
		Assert.assertEquals(0.875f, pkt1.getAccentColor(), EPSILON);
		Assert.assertEquals("Diana", pkt1.getName());
	}
}
