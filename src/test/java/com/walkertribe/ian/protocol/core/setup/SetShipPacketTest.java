package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.Artemis;

public class SetShipPacketTest extends AbstractPacketTester<SetShipPacket> {
	@Test
	public void test() {
		execute("core/setup/SetShipPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(1, new SetShipPacket(1).getShipIndex());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNegativeShipIndex() {
		new SetShipPacket(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructShipIndexTooLarge() {
		new SetShipPacket(Artemis.SHIP_COUNT);
	}

	@Override
	protected void testPackets(List<SetShipPacket> packets) {
		Assert.assertEquals(1, packets.get(0).getShipIndex());
	}

}
