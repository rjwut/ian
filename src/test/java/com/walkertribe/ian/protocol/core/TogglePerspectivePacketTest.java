package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class TogglePerspectivePacketTest extends AbstractPacketTester<TogglePerspectivePacket> {
	@Test
	public void test() {
		execute("core/TogglePerspectivePacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		new TogglePerspectivePacket();
	}

	@Override
	protected void testPackets(List<TogglePerspectivePacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
