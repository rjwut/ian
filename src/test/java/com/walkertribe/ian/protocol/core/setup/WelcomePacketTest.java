package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

import org.junit.Test;

public class WelcomePacketTest extends AbstractPacketTester<WelcomePacket> {
	@Test
	public void test() {
		execute("core/setup/WelcomePacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(WelcomePacket.MSG, new WelcomePacket().getMessage());
		Assert.assertEquals("Hi!", new WelcomePacket("Hi!").getMessage());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullMessage() {
		new WelcomePacket((String) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyMessage() {
		new WelcomePacket("");
	}

	@Override
	protected void testPackets(List<WelcomePacket> packets) {
		Assert.assertEquals(WelcomePacket.MSG, packets.get(0).getMessage());
	}
}