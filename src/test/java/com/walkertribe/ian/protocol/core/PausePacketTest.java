package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class PausePacketTest extends AbstractPacketTester<PausePacket> {
	@Test
	public void test() {
		execute("core/PausePacket.txt", Origin.SERVER, 2);
	}

	@Test
	public void testConstruct() {
		Assert.assertTrue(new PausePacket(true).isPaused());
		Assert.assertFalse(new PausePacket(false).isPaused());
	}

	@Override
	protected void testPackets(List<PausePacket> packets) {
		Assert.assertTrue(packets.get(0).isPaused());
		Assert.assertFalse(packets.get(1).isPaused());
	}
}
