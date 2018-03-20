package com.walkertribe.ian.protocol.core.singleseat;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class SingleSeatShootPacketTest extends AbstractPacketTester<SingleSeatShootPacket> {
	@Test
	public void test() {
		execute("core/singleseat/SingleSeatShootPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		test(new SingleSeatShootPacket(1000, 1001));
	}

	@Override
	protected void testPackets(List<SingleSeatShootPacket> packets) {
		test(packets.get(0));
	}

	private void test(SingleSeatShootPacket pkt) {
		Assert.assertEquals(1000, pkt.getShooterId());
		Assert.assertEquals(1001, pkt.getTargetId());
	}
}
