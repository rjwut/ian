package com.walkertribe.ian.protocol.core.eng;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class EngSetCoolantPacketTest extends AbstractPacketTester<EngSetCoolantPacket> {
	@Test
	public void test() {
		execute("core/eng/EngSetCoolantPacket.txt", ConnectionType.CLIENT, 2);
	}

	@Override
	protected void testPackets(List<EngSetCoolantPacket> packets) {
		EngSetCoolantPacket pkt = packets.get(0);
		Assert.assertEquals(ShipSystem.AFT_SHIELDS, pkt.getSystem());
		Assert.assertEquals(3, pkt.getCoolant());
		pkt = packets.get(1);
		Assert.assertEquals(ShipSystem.BEAMS, pkt.getSystem());
		Assert.assertEquals(0, pkt.getCoolant());
	}
}