package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class ToggleShieldsPacketTest extends AbstractPacketTester<ToggleShieldsPacket> {
	@Test
	public void test() {
		execute("core/ToggleShieldsPacket.txt", ConnectionType.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		new ToggleShieldsPacket();
	}

	@Override
	protected void testPackets(List<ToggleShieldsPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
