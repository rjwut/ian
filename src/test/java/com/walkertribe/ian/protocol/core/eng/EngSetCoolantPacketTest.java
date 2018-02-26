package com.walkertribe.ian.protocol.core.eng;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class EngSetCoolantPacketTest extends AbstractPacketTester<EngSetCoolantPacket> {
	@Test
	public void test() {
		execute("core/eng/EngSetCoolantPacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		EngSetCoolantPacket pkt = new EngSetCoolantPacket(ShipSystem.AFT_SHIELDS, 3);
		Assert.assertEquals(ShipSystem.AFT_SHIELDS, pkt.getSystem());
		Assert.assertEquals(3, pkt.getCoolant());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullSystem() {
		new EngSetCoolantPacket(null, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNegativeCoolant() {
		new EngSetCoolantPacket(ShipSystem.AFT_SHIELDS, -1);
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