package com.walkertribe.ian.protocol.core.weap;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.OrdnanceType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.Artemis;

public class LoadTubePacketTest extends AbstractPacketTester<LoadTubePacket> {
	@Test
	public void test() {
		execute("core/weap/LoadTubePacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		test(new LoadTubePacket(1, OrdnanceType.MINE));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testConstructTubeIndexTooLow() {
		new LoadTubePacket(-1, OrdnanceType.MINE);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testConstructTubeIndexTooHigh() {
		new LoadTubePacket(Artemis.MAX_TUBES, OrdnanceType.MINE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullOrdnance() {
		new LoadTubePacket(0, null);
	}

	@Override
	protected void testPackets(List<LoadTubePacket> packets) {
		test(packets.get(0));
	}

	private void test(LoadTubePacket pkt) {
		Assert.assertEquals(1, pkt.getTubeIndex());
		Assert.assertEquals(OrdnanceType.MINE, pkt.getOrdnanceType());
	}
}
