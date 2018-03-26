package com.walkertribe.ian.protocol.core.gm;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class GameMasterMessagePacketTest extends AbstractPacketTester<GameMasterMessagePacket> {
	@Test
	public void test() {
		execute("core/gm/GameMasterMessagePacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		GameMasterMessagePacket pkt0 = new GameMasterMessagePacket();
		pkt0.setRecipient(0, true);
		pkt0.setSender("Sender");
		pkt0.setMessage("Message");
		pkt0.setConsole(null);
		GameMasterMessagePacket pkt1 = new GameMasterMessagePacket();
		pkt1.setRecipient(1, true);
		pkt1.setRecipient(2, true);
		pkt1.setConsole(Console.MAIN_SCREEN);
		pkt1.setSender("Sender");
		pkt1.setMessage("Message");
		test(pkt0, pkt1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullSender() {
		new GameMasterMessagePacket().setSender(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBlankSender() {
		new GameMasterMessagePacket().setSender("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullMessage() {
		new GameMasterMessagePacket().setMessage(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBlankMessage() {
		new GameMasterMessagePacket().setMessage("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConsole() {
		new GameMasterMessagePacket().setConsole(Console.CAPTAINS_MAP);
	}

	@Override
	protected void testPackets(List<GameMasterMessagePacket> packets) {
		test(packets.get(0), packets.get(1));
	}

	private void test(GameMasterMessagePacket pkt0, GameMasterMessagePacket pkt1) {
		Set<Integer> recipients = pkt0.getRecipients();
		Assert.assertEquals(1, recipients.size());
		Assert.assertTrue(recipients.contains(0));
		TestUtil.assertToStringEquals("Sender", pkt0.getSender());
		TestUtil.assertToStringEquals("Message", pkt0.getMessage());
		Assert.assertNull(pkt0.getConsole());
		recipients = pkt1.getRecipients();
		Assert.assertEquals(2, recipients.size());
		Assert.assertTrue(recipients.contains(1));
		Assert.assertTrue(recipients.contains(2));
		TestUtil.assertToStringEquals("Sender", pkt1.getSender());
		TestUtil.assertToStringEquals("Message", pkt1.getMessage());
		Assert.assertEquals(Console.MAIN_SCREEN, pkt1.getConsole());
	}
}
