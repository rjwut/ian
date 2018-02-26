package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.Artemis;

public class HelmSetWarpPacketTest extends AbstractPacketTester<HelmSetWarpPacket> {
	@Test
	public void test() {
		execute("core/helm/HelmSetWarpPacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		for (int warp = 0; warp <= Artemis.MAX_WARP; warp++) {
			new HelmSetWarpPacket(warp);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNegativeWarp() {
		new HelmSetWarpPacket(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructPastMaxWarp() {
		new HelmSetWarpPacket(Artemis.MAX_WARP + 1);
	}

	@Override
	protected void testPackets(List<HelmSetWarpPacket> packets) {
		Assert.assertEquals(0, packets.get(0).getWarpFactor());
		Assert.assertEquals(4, packets.get(1).getWarpFactor());
	}

}
