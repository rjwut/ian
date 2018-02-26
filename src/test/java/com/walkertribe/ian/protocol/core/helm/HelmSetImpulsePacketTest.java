package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class HelmSetImpulsePacketTest extends AbstractPacketTester<HelmSetImpulsePacket> {
	@Test
	public void test() {
		execute("core/helm/HelmSetImpulsePacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(0.0f, new HelmSetImpulsePacket(0.0f).getPower(), EPSILON);
		Assert.assertEquals(1.0f, new HelmSetImpulsePacket(1.0f).getPower(), EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNegative() {
		new HelmSetImpulsePacket(-0.01f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructTooMuchPower() {
		new HelmSetImpulsePacket(1.01f);
	}

	@Override
	protected void testPackets(List<HelmSetImpulsePacket> packets) {
		HelmSetImpulsePacket pkt = packets.get(0);
		Assert.assertEquals(0.0f, pkt.getPower(), EPSILON);
		pkt = packets.get(1);
		Assert.assertEquals(0.7f, pkt.getPower(), EPSILON);
	}
}