package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class ReadyPacketTest extends AbstractPacketTester<ReadyPacket> {
	@Test
	public void test() {
		execute("core/setup/ReadyPacket.txt", Origin.CLIENT, 1);
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