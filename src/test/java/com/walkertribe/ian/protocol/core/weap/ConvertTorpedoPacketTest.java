package com.walkertribe.ian.protocol.core.weap;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.protocol.core.weap.ConvertTorpedoPacket.Direction;
import com.walkertribe.ian.util.TestUtil;

public class ConvertTorpedoPacketTest extends AbstractPacketTester<ConvertTorpedoPacket> {
	@Test
	public void test() {
		execute("core/weap/ConvertTorpedoPacket.txt", Origin.CLIENT, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullDirection() {
		new ConvertTorpedoPacket((ConvertTorpedoPacket.Direction) null);
	}

	@Test
	public void testConstruct() {
		test(
				new ConvertTorpedoPacket(Direction.TORPEDO_TO_ENERGY),
				new ConvertTorpedoPacket(Direction.ENERGY_TO_TORPEDO)
		);
	}

	@Override
	protected void testPackets(List<ConvertTorpedoPacket> packets) {
		test(packets.get(0), packets.get(1));
	}

	@Test
	public void makeEclEmmaHappy() {
		TestUtil.coverEnumValueOf(ConvertTorpedoPacket.Direction.class);
	}

	private void test(ConvertTorpedoPacket pkt0, ConvertTorpedoPacket pkt1) {
		Assert.assertEquals(Direction.TORPEDO_TO_ENERGY, pkt0.getDirection());
		Assert.assertEquals(Direction.ENERGY_TO_TORPEDO, pkt1.getDirection());
	}
}
