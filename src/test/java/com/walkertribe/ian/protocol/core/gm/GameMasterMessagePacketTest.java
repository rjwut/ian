package com.walkertribe.ian.protocol.core.gm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class GameMasterMessagePacketTest extends AbstractPacketTester<GameMasterMessagePacket> {
	@Test
	public void test() {
		execute("core/gm/GameMasterMessagePacket.txt", ConnectionType.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		test(
				new GameMasterMessagePacket("Sender", "Message"),
				new GameMasterMessagePacket("Sender", "Message", Console.MAIN_SCREEN)
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullSender() {
		new GameMasterMessagePacket(null, "Message");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructBlankSender() {
		new GameMasterMessagePacket("", "Message");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullMessage() {
		new GameMasterMessagePacket("Sender", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructBlankMessage() {
		new GameMasterMessagePacket("Sender", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidConsole() {
		new GameMasterMessagePacket("Sender", "Message", Console.CAPTAINS_MAP);
	}

	@Override
	protected void testPackets(List<GameMasterMessagePacket> packets) {
		test(packets.get(0), packets.get(1));
	}

	private void test(GameMasterMessagePacket pkt0, GameMasterMessagePacket pkt1) {
		TestUtil.assertToStringEquals("Sender", pkt0.getSender());
		TestUtil.assertToStringEquals("Message", pkt0.getMessage());
		Assert.assertNull(pkt0.getConsole());
		TestUtil.assertToStringEquals("Sender", pkt1.getSender());
		TestUtil.assertToStringEquals("Message", pkt1.getMessage());
		Assert.assertEquals(Console.MAIN_SCREEN, pkt1.getConsole());
	}
}
