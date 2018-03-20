package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class CloakDecloakPacketTest extends AbstractPacketTester<CloakDecloakPacket> {
	@Test
	public void test() {
		execute("core/world/CloakDecloakPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		test(new CloakDecloakPacket(1, 2, 3));
	}

	@Override
	protected void testPackets(List<CloakDecloakPacket> packets) {
		test(packets.get(0));
	}

	private void test(CloakDecloakPacket pkt) {
		Assert.assertEquals(1, pkt.getX(), TestUtil.EPSILON);
		Assert.assertEquals(2, pkt.getY(), TestUtil.EPSILON);
		Assert.assertEquals(3, pkt.getZ(), TestUtil.EPSILON);
	}
}
