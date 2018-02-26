package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.BoolState;

public class PausePacketTest extends AbstractPacketTester<PausePacket> {
	@Test
	public void test() {
		execute("core/PausePacket.txt", Origin.SERVER, 2);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(BoolState.TRUE, new PausePacket(true).getPaused());
		Assert.assertEquals(BoolState.FALSE, new PausePacket(false).getPaused());
	}

	@Override
	protected void testPackets(List<PausePacket> packets) {
		Assert.assertEquals(BoolState.TRUE, packets.get(0).getPaused());
		Assert.assertEquals(BoolState.FALSE, packets.get(1).getPaused());
	}
}
