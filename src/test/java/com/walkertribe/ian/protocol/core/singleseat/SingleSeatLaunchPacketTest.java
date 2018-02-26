package com.walkertribe.ian.protocol.core.singleseat;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.protocol.core.singleseat.SingleSeatLaunchPacket;

public class SingleSeatLaunchPacketTest extends AbstractPacketTester<SingleSeatLaunchPacket> {
	@Test
	public void test() {
		execute("core/singleseat/SingleSeatLaunchPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(1, new SingleSeatLaunchPacket(1).getObjectId());
	}

	@Override
	protected void testPackets(List<SingleSeatLaunchPacket> packets) {
		Assert.assertEquals(1, packets.get(0).getObjectId());
	}
}
