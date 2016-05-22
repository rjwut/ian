package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class HelmJumpPacketTest extends AbstractPacketTester<HelmJumpPacket> {
	@Test
	public void test() {
		execute("core/helm/HelmJumpPacket.txt", ConnectionType.CLIENT, 1);
	}

	@Override
	protected void testPackets(List<HelmJumpPacket> packets) {
		HelmJumpPacket pkt = packets.get(0);
		Assert.assertEquals(0.2f, pkt.getHeading(), EPSILON);
		Assert.assertEquals(0.7f, pkt.getDistance(), EPSILON);
	}
}
