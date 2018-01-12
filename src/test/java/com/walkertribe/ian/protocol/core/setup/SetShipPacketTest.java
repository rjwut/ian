package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.Artemis;

public class SetShipPacketTest extends AbstractPacketTester<SetShipPacket> {
	@Test
	public void test() {
		execute("core/setup/SetShipPacket.txt", ConnectionType.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(2, new SetShipPacket(2).getShipNumber());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructZeroShipNumber() {
		new SetShipPacket(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructShipNumberTooLarge() {
		new SetShipPacket(Artemis.SHIP_COUNT + 1);
	}

	@Override
	protected void testPackets(List<SetShipPacket> packets) {
		Assert.assertEquals(2, packets.get(0).getShipNumber());
	}

}
