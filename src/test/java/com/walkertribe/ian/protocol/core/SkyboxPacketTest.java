package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class SkyboxPacketTest extends AbstractPacketTester<SkyboxPacket> {
	@Test
	public void test() {
		execute("core/SkyboxPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(7, new SkyboxPacket(7).getSkyboxId());
	}

	@Override
	protected void testPackets(List<SkyboxPacket> packets) {
		SkyboxPacket pkt = packets.get(0);
		Assert.assertEquals(2, pkt.getSkyboxId());
	}
}
