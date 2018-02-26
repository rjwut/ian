package com.walkertribe.ian.protocol.core.comm;

import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class ToggleRedAlertPacketTest extends AbstractPacketTester<ToggleRedAlertPacket> {
	@Test
	public void testParse() {
		execute("core/comm/ToggleRedAlertPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		new ToggleRedAlertPacket();
	}

	@Override
	protected void testPackets(List<ToggleRedAlertPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}