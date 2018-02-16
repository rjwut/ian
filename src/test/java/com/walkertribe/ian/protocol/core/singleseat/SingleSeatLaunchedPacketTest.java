package com.walkertribe.ian.protocol.core.singleseat;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.protocol.core.singleseat.SingleSeatLaunchedPacket;

public class SingleSeatLaunchedPacketTest extends AbstractPacketTester<SingleSeatLaunchedPacket> {
	@Test
	public void test() {
		execute("core/fighter/SingleSeatLaunchedPacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(1, new SingleSeatLaunchedPacket(1).getObjectId());
	}

	@Override
	protected void testPackets(List<SingleSeatLaunchedPacket> packets) {
		Assert.assertEquals(1, packets.get(0).getObjectId());
	}

}
