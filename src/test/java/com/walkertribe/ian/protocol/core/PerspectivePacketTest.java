package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class PerspectivePacketTest extends AbstractPacketTester<PerspectivePacket> {
	@Test
	public void test() {
		execute("core/PerspectivePacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		new PerspectivePacket();
	}

	@Override
	protected void testPackets(List<PerspectivePacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}