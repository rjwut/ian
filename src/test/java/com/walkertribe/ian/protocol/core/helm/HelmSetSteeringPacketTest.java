package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class HelmSetSteeringPacketTest extends AbstractPacketTester<HelmSetSteeringPacket> {
	@Test
	public void test() {
		execute("core/helm/HelmSetSteeringPacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(0.0f, new HelmSetSteeringPacket(0.0f).getSteering(), EPSILON);
		Assert.assertEquals(1.0f, new HelmSetSteeringPacket(1.0f).getSteering(), EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructTooHardPort() {
		new HelmSetSteeringPacket(-0.01f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructTooHardStarboard() {
		new HelmSetSteeringPacket(1.01f);
	}

	@Override
	protected void testPackets(List<HelmSetSteeringPacket> packets) {
		HelmSetSteeringPacket pkt = packets.get(0);
		Assert.assertEquals(0.0f, pkt.getSteering(), EPSILON);
		pkt = packets.get(1);
		Assert.assertEquals(0.7f, pkt.getSteering(), EPSILON);
	}
}