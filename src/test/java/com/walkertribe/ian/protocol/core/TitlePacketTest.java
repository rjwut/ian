package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class TitlePacketTest extends AbstractPacketTester<TitlePacket> {
	@Test
	public void test() {
		execute("core/TitlePacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		TitlePacket pkt = new TitlePacket("test1", "test2", "test3");
		Assert.assertEquals("test1", pkt.getTitle());
		Assert.assertEquals("test2", pkt.getSubtitle1());
		Assert.assertEquals("test3", pkt.getSubtitle2());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullTitle() {
		new TitlePacket(null, "test2", "test3");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyTitle() {
		new TitlePacket("", "test2", "test3");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullSubtitle1() {
		new TitlePacket("test1", null, "test3");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptySubtitle1() {
		new TitlePacket("test1", "", "test3");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullSubtitle2() {
		new TitlePacket("test1", "test2", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptySubtitle2() {
		new TitlePacket("test1", "test2", "");
	}

	@Override
	protected void testPackets(List<TitlePacket> packets) {
		TitlePacket pkt = packets.get(0);
		TestUtil.assertToStringEquals("test1", pkt.getTitle());
		TestUtil.assertToStringEquals("test2", pkt.getSubtitle1());
		TestUtil.assertToStringEquals("test3", pkt.getSubtitle2());
	}
}
