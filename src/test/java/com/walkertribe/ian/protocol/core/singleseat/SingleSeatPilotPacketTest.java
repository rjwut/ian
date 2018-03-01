package com.walkertribe.ian.protocol.core.singleseat;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class SingleSeatPilotPacketTest extends AbstractPacketTester<SingleSeatPilotPacket> {
	@Test
	public void test() {
		execute("core/singleseat/SingleSeatPilotPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		SingleSeatPilotPacket pkt = new SingleSeatPilotPacket(47);
		pkt.setRudder(0.5f);
		pkt.setX(1f);
		pkt.setY(2f);
		pkt.setZ(3f);
		pkt.setOrientX(4f);
		pkt.setOrientY(5f);
		pkt.setOrientZ(6f);
		pkt.setOrientW(7f);
		test(pkt);
	}

	@Override
	protected void testPackets(List<SingleSeatPilotPacket> packets) {
		test(packets.get(0));
	}

	private void test(SingleSeatPilotPacket pkt) {
		Assert.assertEquals(47, pkt.getObjectId());
		Assert.assertEquals(0.5f, pkt.getRudder(), EPSILON);
		Assert.assertEquals(1f, pkt.getX(), EPSILON);
		Assert.assertEquals(2f, pkt.getY(), EPSILON);
		Assert.assertEquals(3f, pkt.getZ(), EPSILON);
		Assert.assertEquals(4f, pkt.getOrientX(), EPSILON);
		Assert.assertEquals(5f, pkt.getOrientY(), EPSILON);
		Assert.assertEquals(6f, pkt.getOrientZ(), EPSILON);
		Assert.assertEquals(7f, pkt.getOrientW(), EPSILON);
	}
}
