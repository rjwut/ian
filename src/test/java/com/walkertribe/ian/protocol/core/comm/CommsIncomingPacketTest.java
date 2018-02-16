package com.walkertribe.ian.protocol.core.comm;

import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class CommsIncomingPacketTest extends AbstractPacketTester<CommsIncomingPacket> {
	@Test
	public void testParse() {
		execute("core/comm/CommsIncomingPacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		new CommsIncomingPacket(CommsIncomingPacket.MIN_PRIORITY_VALUE, "From", "Message");
		new CommsIncomingPacket(CommsIncomingPacket.MAX_PRIORITY_VALUE, "From", "Message");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPriorityLow() {
		new CommsIncomingPacket(CommsIncomingPacket.MIN_PRIORITY_VALUE - 1, "From", "Message");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPriorityHigh() {
		new CommsIncomingPacket(CommsIncomingPacket.MAX_PRIORITY_VALUE + 1, "From", "Message");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullFrom() {
		new CommsIncomingPacket(CommsIncomingPacket.MIN_PRIORITY_VALUE, null, "Message");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyFrom() {
		new CommsIncomingPacket(CommsIncomingPacket.MIN_PRIORITY_VALUE, "", "Message");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullMessage() {
		new CommsIncomingPacket(CommsIncomingPacket.MIN_PRIORITY_VALUE, "From", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyMessage() {
		new CommsIncomingPacket(CommsIncomingPacket.MIN_PRIORITY_VALUE, "From", "");
	}

	@Override
	protected void testPackets(List<CommsIncomingPacket> packets) {
		CommsIncomingPacket pkt = packets.get(0);
		Assert.assertEquals(0, pkt.getPriority());
		TestUtil.assertToStringEquals("DS1", pkt.getFrom());
		TestUtil.assertToStringEquals("Hi.\nHow are you?", pkt.getMessage());
	}
}