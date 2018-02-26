package com.walkertribe.ian.protocol.core.weap;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.Artemis;

public class UnloadTubePacketTest extends AbstractPacketTester<UnloadTubePacket> {
	@Test
	public void test() {
		execute("core/weap/UnloadTubePacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		test(new UnloadTubePacket(1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testConstructTubeIndexTooLow() {
		new UnloadTubePacket(-1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testConstructTubeIndexTooHigh() {
		new UnloadTubePacket(Artemis.MAX_TUBES);
	}

	@Override
	protected void testPackets(List<UnloadTubePacket> packets) {
		test(packets.get(0));
	}

	private void test(UnloadTubePacket pkt) {
		Assert.assertEquals(1, pkt.getTubeIndex());
	}
}
