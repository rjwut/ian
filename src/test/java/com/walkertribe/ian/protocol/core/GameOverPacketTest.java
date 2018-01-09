package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class GameOverPacketTest extends AbstractPacketTester<GameOverPacket> {
	@Test
	public void test() {
		execute("core/GameOverPacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		new GameOverPacket();
	}

	@Override
	protected void testPackets(List<GameOverPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
