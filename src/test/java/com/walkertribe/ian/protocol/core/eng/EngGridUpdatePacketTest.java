package com.walkertribe.ian.protocol.core.eng;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.GridCoord;

public class EngGridUpdatePacketTest extends AbstractPacketTester<EngGridUpdatePacket> {
	@Test
	public void test() {
		execute("core/eng/EngGridUpdatePacket.txt", ConnectionType.SERVER, 1);
	}

	@Override
	protected void testPackets(List<EngGridUpdatePacket> packets) {
		EngGridUpdatePacket pkt = packets.get(0);
		List<EngGridUpdatePacket.GridDamage> damages = pkt.getDamage();
		Assert.assertEquals(2, damages.size());
		EngGridUpdatePacket.GridDamage damage = damages.get(0);
		GridCoord coord = damage.getCoord();
		Assert.assertEquals(0, coord.getX());
		Assert.assertEquals(0, coord.getY());
		Assert.assertEquals(0, coord.getZ());
		Assert.assertEquals(0.0f, damage.getDamage(), EPSILON);
		damage = damages.get(1);
		coord = damage.getCoord();
		Assert.assertEquals(1, coord.getX());
		Assert.assertEquals(-1, coord.getY());
		Assert.assertEquals(1, coord.getZ());
		Assert.assertEquals(0.7f, damage.getDamage(), EPSILON);
		List<EngGridUpdatePacket.DamconStatus> statuses = pkt.getDamcons();
		Assert.assertEquals(1, statuses.size());
		EngGridUpdatePacket.DamconStatus status = statuses.get(0);
		Assert.assertEquals(0, status.getTeamNumber());
		Assert.assertEquals(3, status.getMembers());
		Assert.assertEquals(0.3f, status.getProgress(), EPSILON);
		coord = status.getPosition();
		Assert.assertEquals(3, coord.getX());
		Assert.assertEquals(4, coord.getY());
		Assert.assertEquals(0, coord.getZ());
		coord = status.getGoal();
		Assert.assertEquals(4, coord.getX());
		Assert.assertEquals(7, coord.getY());
		Assert.assertEquals(-1, coord.getZ());
	}
}
