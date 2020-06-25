package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class ServerHeartbeatPacketTest extends AbstractPacketTester<ServerHeartbeatPacket> {
	@Test
	public void test() {
		execute("core/ServerHeartbeatPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		new ServerHeartbeatPacket();
	}

	@Override
	protected void testPackets(List<ServerHeartbeatPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
