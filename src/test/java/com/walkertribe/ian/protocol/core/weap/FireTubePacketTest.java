package com.walkertribe.ian.protocol.core.weap;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.Artemis;

public class FireTubePacketTest extends AbstractPacketTester<FireTubePacket> {
	@Test
	public void test() {
		execute("core/weap/FireTubePacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		test(new FireTubePacket(1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testConstructTubeIndexTooLow() {
		new FireTubePacket(-1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testConstructTubeIndexTooHigh() {
		new FireTubePacket(Artemis.MAX_TUBES);
	}

	@Override
	protected void testPackets(List<FireTubePacket> packets) {
		test(packets.get(0));
	}

	private void test(FireTubePacket pkt) {
		Assert.assertEquals(1, pkt.getTubeIndex());
	}
}
