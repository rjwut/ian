package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class HeartbeatPacketTest extends AbstractPacketTester<HeartbeatPacket> {
	@Test
	public void test() {
		execute("core/HeartbeatPacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		new HeartbeatPacket();
	}

	@Override
	protected void testPackets(List<HeartbeatPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
