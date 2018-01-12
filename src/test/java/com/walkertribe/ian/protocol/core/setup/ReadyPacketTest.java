package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class ReadyPacketTest extends AbstractPacketTester<ReadyPacket> {
	@Test
	public void test() {
		execute("core/setup/ReadyPacket.txt", ConnectionType.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		new ReadyPacket();
	}

	@Override
	protected void testPackets(List<ReadyPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}

}