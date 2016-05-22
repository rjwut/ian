package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class HelmSetImpulsePacketTest extends AbstractPacketTester<HelmSetImpulsePacket> {
	@Test
	public void test() {
		execute("core/helm/HelmSetImpulsePacket.txt", ConnectionType.CLIENT, 2);
	}

	@Override
	protected void testPackets(List<HelmSetImpulsePacket> packets) {
		HelmSetImpulsePacket pkt = packets.get(0);
		Assert.assertEquals(0.0f, pkt.getPower(), EPSILON);
		pkt = packets.get(1);
		Assert.assertEquals(0.7f, pkt.getPower(), EPSILON);
	}
}