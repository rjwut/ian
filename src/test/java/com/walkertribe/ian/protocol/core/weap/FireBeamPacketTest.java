package com.walkertribe.ian.protocol.core.weap;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.ArtemisCreature;

public class FireBeamPacketTest extends AbstractPacketTester<FireBeamPacket> {
	@Test
	public void test() {
		execute("core/weap/FireBeamPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		test(new FireBeamPacket(new ArtemisCreature(1), 1.0f, 2.0f, 3.0f));
	}

	@Override
	protected void testPackets(List<FireBeamPacket> packets) {
		test(packets.get(0));
	}

	private void test(FireBeamPacket pkt) {
		Assert.assertEquals(1, pkt.getTargetId());
		Assert.assertEquals(1.0, pkt.getX(), EPSILON);
		Assert.assertEquals(2.0, pkt.getY(), EPSILON);
		Assert.assertEquals(3.0, pkt.getZ(), EPSILON);
	}
}
