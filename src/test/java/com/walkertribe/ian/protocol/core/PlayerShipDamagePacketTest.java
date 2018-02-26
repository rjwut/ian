package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class PlayerShipDamagePacketTest extends AbstractPacketTester<PlayerShipDamagePacket> {
	@Test
	public void test() {
		execute("core/PlayerShipDamagePacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		new PlayerShipDamagePacket(0, 1.5f);
	}

	@Override
	protected void testPackets(List<PlayerShipDamagePacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
