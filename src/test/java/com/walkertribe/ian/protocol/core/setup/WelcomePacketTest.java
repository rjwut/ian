package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

import org.junit.Test;

public class WelcomePacketTest extends AbstractPacketTester<WelcomePacket> {
	@Test
	public void test() {
		execute("core/setup/WelcomePacket.txt", ConnectionType.SERVER, 1);
	}

	@Override
	protected void testPackets(List<WelcomePacket> packets) {
		Assert.assertEquals(WelcomePacket.MSG, packets.get(0).getMessage());
	}
}