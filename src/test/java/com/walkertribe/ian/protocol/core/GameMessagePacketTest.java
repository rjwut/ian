package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class GameMessagePacketTest extends AbstractPacketTester<GameMessagePacket> {
	@Test
	public void test() {
		execute("core/GameMessagePacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals("Hi!", new GameMessagePacket("Hi!").getMessage());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNull() {
		new GameMessagePacket(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyString() {
		new GameMessagePacket("");
	}

	@Override
	protected void testPackets(List<GameMessagePacket> packets) {
		TestUtil.assertToStringEquals("Hi!", packets.get(0).getMessage());
	}
}
