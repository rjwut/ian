package com.walkertribe.ian.protocol.core.eng;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.GridCoord;

public class EngSendDamconPacketTest extends AbstractPacketTester<EngSendDamconPacket> {
	@Test
	public void testParse() {
		execute("core/eng/EngSendDamconPacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		new EngSendDamconPacket(0, GridCoord.getInstance(0, 0, 0));
		new EngSendDamconPacket(0, 0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidDamconTeamNumber() {
		new EngSendDamconPacket(-1, 0, 0, 0);
	}

	@Override
	protected void testPackets(List<EngSendDamconPacket> packets) {
		EngSendDamconPacket pkt = packets.get(0);
		Assert.assertEquals(0, pkt.getTeamNumber());
		GridCoord coord = pkt.getDestination();
		Assert.assertEquals(0, coord.getX());
		Assert.assertEquals(0, coord.getY());
		Assert.assertEquals(0, coord.getZ());
		pkt = packets.get(1);
		Assert.assertEquals(1, pkt.getTeamNumber());
		coord = pkt.getDestination();
		Assert.assertEquals(2, coord.getX());
		Assert.assertEquals(3, coord.getY());
		Assert.assertEquals(4, coord.getZ());
	}
}