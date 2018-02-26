package com.walkertribe.ian.protocol.core.eng;

import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class EngSetEnergyPacketTest extends AbstractPacketTester<EngSetEnergyPacket> {
	private static final float DEFAULT_ENERGY_ALLOCATION = 1.0f / 3;

	@Test
	public void test() {
		execute("core/eng/EngSetEnergyPacket.txt", Origin.CLIENT, 3);
	}

	@Test
	public void testConstruct() {
		EngSetEnergyPacket pkt = new EngSetEnergyPacket(ShipSystem.AFT_SHIELDS, 1.0f);
		Assert.assertEquals(ShipSystem.AFT_SHIELDS, pkt.getSystem());
		Assert.assertEquals(1.0f, pkt.getAllocation(), TestUtil.EPSILON);
		pkt = new EngSetEnergyPacket(ShipSystem.BEAMS, 150);
		Assert.assertEquals(ShipSystem.BEAMS, pkt.getSystem());
		Assert.assertEquals(0.5f, pkt.getAllocation(), TestUtil.EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullSystemFloatEnergy() {
		new EngSetEnergyPacket(null, 1.0f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNegativeFloatEnergy() {
		new EngSetEnergyPacket(ShipSystem.AFT_SHIELDS, -0.1f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructPastMaxFloatEnergy() {
		new EngSetEnergyPacket(ShipSystem.AFT_SHIELDS, 1.1f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullSystemIntEnergy() {
		new EngSetEnergyPacket(null, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNegativeIntEnergy() {
		new EngSetEnergyPacket(ShipSystem.AFT_SHIELDS, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructPastMaxIntEnergy() {
		new EngSetEnergyPacket(ShipSystem.AFT_SHIELDS, 301);
	}

	@Override
	protected void testPackets(List<EngSetEnergyPacket> packets) {
		EngSetEnergyPacket pkt = packets.get(0);
		Assert.assertEquals(pkt.getSystem(), ShipSystem.BEAMS);
		Assert.assertEquals(DEFAULT_ENERGY_ALLOCATION, pkt.getAllocation(), EPSILON);
		pkt = packets.get(1);
		Assert.assertEquals(pkt.getSystem(), ShipSystem.AFT_SHIELDS);
		Assert.assertEquals(0.0f, pkt.getAllocation(), EPSILON);
		pkt = packets.get(2);
		Assert.assertEquals(pkt.getSystem(), ShipSystem.WARP_JUMP_DRIVE);
		Assert.assertEquals(1.0f, pkt.getAllocation(), EPSILON);
	}
}