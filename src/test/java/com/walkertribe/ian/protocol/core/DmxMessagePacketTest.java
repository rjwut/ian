package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class DmxMessagePacketTest extends AbstractPacketTester<DmxMessagePacket> {
	@Test
	public void test() {
		execute("core/DmxMessagePacket.txt", ConnectionType.SERVER, 2);
	}

	@Test
	public void testConstruct() {
		DmxMessagePacket pkt = new DmxMessagePacket("test1", true);
		Assert.assertEquals("test1", pkt.getName());
		Assert.assertTrue(pkt.isOn());
		pkt = new DmxMessagePacket("test2", false);
		Assert.assertEquals("test2", pkt.getName());
		Assert.assertFalse(pkt.isOn());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullName() {
		new DmxMessagePacket(null, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyName() {
		new DmxMessagePacket("", true);
	}

	@Override
	protected void testPackets(List<DmxMessagePacket> packets) {
		DmxMessagePacket pkt = packets.get(0);
		Assert.assertEquals("test1", pkt.getName());
		Assert.assertTrue(pkt.isOn());
		pkt = packets.get(1);
		Assert.assertEquals("test2", pkt.getName());
		Assert.assertFalse(pkt.isOn());
	}
}