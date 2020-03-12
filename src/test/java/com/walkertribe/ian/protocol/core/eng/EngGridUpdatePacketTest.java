package com.walkertribe.ian.protocol.core.eng;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.GridCoord;
import com.walkertribe.ian.util.TestUtil;

public class EngGridUpdatePacketTest extends AbstractPacketTester<EngGridUpdatePacket> {
	@Test
	public void testParse() {
		execute("core/eng/EngGridUpdatePacket.txt", Origin.SERVER, 2);
	}

	@Test
	public void testConstruct() {
		EngGridUpdatePacket pkt = new EngGridUpdatePacket(false);
		pkt.toString();
		pkt.addDamageUpdate(0, 0, 0, 0.5f);
		pkt.addDamconUpdate((byte) 1, 3, 0, 0, 0, 0, 0, 0, 1.0f);
	}

	@Override
	protected void testPackets(List<EngGridUpdatePacket> packets) {
		boolean requested = true;

		for (EngGridUpdatePacket pkt : packets) {
			Assert.assertEquals(requested, pkt.isRequested());
			List<NodeDamage> damageEntries = pkt.getDamage();
			Assert.assertEquals(2, damageEntries.size());
			NodeDamage damageEntry = damageEntries.get(0);
			GridCoord coord = damageEntry.getCoord();
			Assert.assertEquals(0, coord.x());
			Assert.assertEquals(0, coord.y());
			Assert.assertEquals(0, coord.z());
			Assert.assertEquals(0.0f, damageEntry.getDamage(), EPSILON);
			damageEntry = damageEntries.get(1);
			coord = damageEntry.getCoord();
			Assert.assertEquals(1, coord.x());
			Assert.assertEquals(1, coord.y());
			Assert.assertEquals(1, coord.z());
			Assert.assertEquals(0.7f, damageEntry.getDamage(), EPSILON);
			List<DamconTeam> statuses = pkt.getDamcons();
			Assert.assertEquals(1, statuses.size());
			DamconTeam status = statuses.get(0);
			Assert.assertEquals(0, status.getId());
			Assert.assertEquals(3, status.getMembers());
			Assert.assertEquals(0.3f, status.getProgress(), EPSILON);
			coord = status.getLocation();
			Assert.assertEquals(3, coord.x());
			Assert.assertEquals(1, coord.y());
			Assert.assertEquals(7, coord.z());
			coord = status.getGoal();
			Assert.assertEquals(4, coord.x());
			Assert.assertEquals(1, coord.y());
			Assert.assertEquals(7, coord.z());
			requested = false;
		}
	}

	@Test
	public void testGridDamageEqualsAndHashCode() {
	    NodeDamage dmg0 = new NodeDamage(GridCoord.get(0, 0, 0), 0.5f);
		NodeDamage dmg1 = new NodeDamage(GridCoord.get(0, 0, 0), 0.5f);
		NodeDamage dmg2 = new NodeDamage(GridCoord.get(0, 0, 1), 0.5f);
		TestUtil.testEqualsAndHashCode(dmg0, dmg1, dmg2);
	}
}
