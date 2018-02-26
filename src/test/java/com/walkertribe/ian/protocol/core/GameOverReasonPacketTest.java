package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class GameOverReasonPacketTest extends AbstractPacketTester<GameOverReasonPacket> {
	private static final String LINE1 = "This is a test of the GameOverReasonPacket.";
	private static final String LINE2 = "This is only a test.";

	@Test
	public void test() {
		execute("core/GameOverReasonPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		GameOverReasonPacket pkt = new GameOverReasonPacket(LINE1, LINE2);
		List<CharSequence> lines = pkt.getText();
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
		List<CharSequence> lines = packets.get(0).getText();
		TestUtil.assertToStringEquals("Hi!", lines.get(0));
		TestUtil.assertToStringEquals("Bye!", lines.get(1));
	}
}
