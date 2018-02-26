package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class HelmRequestDockPacketTest extends AbstractPacketTester<HelmRequestDockPacket> {
	@Test
	public void test() {
		execute("core/helm/HelmRequestDockPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		new HelmRequestDockPacket();
	}

	@Override
	protected void testPackets(List<HelmRequestDockPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}