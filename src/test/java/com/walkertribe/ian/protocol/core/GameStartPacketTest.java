package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.GameType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class GameStartPacketTest extends AbstractPacketTester<GameStartPacket> {
	@Test
	public void test() {
		execute("core/GameStartPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		test(new GameStartPacket(1, GameType.DOUBLE_FRONT));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructDifficultyTooLow() {
		new GameStartPacket(GameStartPacket.MIN_DIFFICULTY - 1, GameType.SIEGE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructDifficultyTooHigh() {
		new GameStartPacket(GameStartPacket.MAX_DIFFICULTY + 1, GameType.SIEGE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullGameType() {
		new GameStartPacket(GameStartPacket.MIN_DIFFICULTY, null);
	}

	@Override
	protected void testPackets(List<GameStartPacket> packets) {
		test(packets.get(0));
	}

	private void test(GameStartPacket pkt) {
		Assert.assertEquals(1, pkt.getDifficulty());
		Assert.assertEquals(GameType.DOUBLE_FRONT, pkt.getGameType());
	}
}
