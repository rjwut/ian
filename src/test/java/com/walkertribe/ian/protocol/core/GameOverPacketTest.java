package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class GameOverPacketTest extends AbstractPacketTester<EndGamePacket> {
	@Test
	public void test() {
		execute("core/GameOverPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		new EndGamePacket();
	}

	@Override
	protected void testPackets(List<EndGamePacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
