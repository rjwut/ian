package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class GameOverReasonPacketTest extends AbstractPacketTester<GameOverReasonPacket> {
	private static final String LINE1 = "This is a test of the GameOverReasonPacket.";
	private static final String LINE2 = "This is only a test.";

	@Test
	public void test() {
		execute("core/GameOverReasonPacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		GameOverReasonPacket pkt = new GameOverReasonPacket(LINE1, LINE2);
		List<String> lines = pkt.getText();
		Assert.assertEquals(LINE1, lines.get(0));
		Assert.assertEquals(LINE2, lines.get(1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmpty() {
		new GameOverReasonPacket();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullLine() {
		new GameOverReasonPacket(LINE1, null, LINE2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructBlankLine() {
		new GameOverReasonPacket(LINE1, "", LINE2);
	}

	@Override
	protected void testPackets(List<GameOverReasonPacket> packets) {
		List<String> lines = packets.get(0).getText();
		Assert.assertEquals("Hi!", lines.get(0));
		Assert.assertEquals("Bye!", lines.get(1));
	}
}
