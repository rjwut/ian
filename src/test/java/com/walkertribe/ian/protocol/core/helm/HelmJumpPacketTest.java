package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class HelmJumpPacketTest extends AbstractPacketTester<HelmJumpPacket> {
	@Test
	public void test() {
		execute("core/helm/HelmJumpPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		HelmJumpPacket pkt = new HelmJumpPacket(0.0f, 1.0f);
		Assert.assertEquals(0.0f, pkt.getHeading(), EPSILON);
		Assert.assertEquals(1.0f, pkt.getDistance(), EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructHeadingTooSmall() {
		new HelmJumpPacket(-0.01f, 0.0f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructHeadingTooLarge() {
		new HelmJumpPacket(1.01f, 0.0f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructDistanceTooSmall() {
		new HelmJumpPacket(0.0f, -0.01f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructDistanceTooLarge() {
		new HelmJumpPacket(0.0f, 1.01f);
	}

	@Override
	protected void testPackets(List<HelmJumpPacket> packets) {
		HelmJumpPacket pkt = packets.get(0);
		Assert.assertEquals(0.2f, pkt.getHeading(), EPSILON);
		Assert.assertEquals(0.7f, pkt.getDistance(), EPSILON);
	}
}
