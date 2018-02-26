package com.walkertribe.ian.protocol.core;

import java.awt.event.KeyEvent;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class KeystrokePacketTest extends AbstractPacketTester<KeystrokePacket> {
	@Test
	public void test() {
		execute("core/KeystrokePacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(KeyEvent.VK_SPACE, new KeystrokePacket(KeyEvent.VK_SPACE).getKeycode());
	}

	@Override
	protected void testPackets(List<KeystrokePacket> packets) {
		KeystrokePacket pkt = packets.get(0);
		Assert.assertEquals(KeyEvent.VK_A, pkt.getKeycode());
	}
}
