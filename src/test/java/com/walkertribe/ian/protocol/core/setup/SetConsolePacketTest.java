package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class SetConsolePacketTest extends AbstractPacketTester<SetConsolePacket> {
	@Test
	public void test() {
		execute("core/setup/SetConsolePacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		for (Console console : Console.values()) {
			SetConsolePacket pkt = new SetConsolePacket(console, true);
			Assert.assertEquals(console, pkt.getConsole());
			Assert.assertTrue(pkt.isSelected());
			pkt = new SetConsolePacket(console, false);
			Assert.assertEquals(console, pkt.getConsole());
			Assert.assertFalse(pkt.isSelected());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullConsole() {
		new SetConsolePacket(null, true);
	}

	@Override
	protected void testPackets(List<SetConsolePacket> packets) {
		SetConsolePacket pkt = packets.get(0);
		Assert.assertEquals(Console.HELM, pkt.getConsole());
		Assert.assertTrue(pkt.isSelected());
		pkt = packets.get(1);
		Assert.assertEquals(Console.COMMUNICATIONS, pkt.getConsole());
		Assert.assertFalse(pkt.isSelected());
	}

}
