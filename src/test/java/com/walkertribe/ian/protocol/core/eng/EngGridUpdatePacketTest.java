package com.walkertribe.ian.protocol.core.eng;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.protocol.core.eng.EngGridUpdatePacket.DamconStatus;
import com.walkertribe.ian.protocol.core.eng.EngGridUpdatePacket.GridDamage;
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
		pkt.addDamconUpdate(1, 3, 0, 0, 0, 0, 0, 0, 1.0f);
	}

	@Override
	protected void testPackets(List<EngGridUpdatePacket> packets) {
		boolean requested = true;

		for (EngGridUpdatePacket pkt : packets) {
			Assert.assertEquals(requested, pkt.isRequested());
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
			requested = false;
		}
	}

	@Test
	public void testGridDamageEqualsAndHashCode() {
		GridDamage dmg0 = new GridDamage(GridCoord.getInstance(0, 0, 0), 0.5f);
		GridDamage dmg1 = new GridDamage(GridCoord.getInstance(0, 0, 0), 0.5f);
		GridDamage dmg2 = new GridDamage(GridCoord.getInstance(0, 0, 1), 0.5f);
		TestUtil.testEqualsAndHashCode(dmg0, dmg1, dmg2);
	}

	@Test
	public void testDamconStatusUpdateFrom() {
		DamconStatus status0 = new DamconStatus(0, 3, 1, 1, 1, 0, 0, 0, 0.0f);
		Assert.assertEquals("Team #0 (3): [0,0,0] => [1,1,1] (0.0)", status0.toString());
		DamconStatus status1 = new DamconStatus(0, 2, 2, 2, 2, 1, 1, 1, 0.5f);
		Assert.assertEquals("Team #0 (2): [1,1,1] => [2,2,2] (0.5)", status1.toString());
		status0.updateFrom(status1);
		Assert.assertEquals("Team #0 (2): [1,1,1] => [2,2,2] (0.5)", status0.toString());
		status1 = new DamconStatus(0, 2, 2, 2, 2, 1, 1, 1, 0);
		status0.updateFrom(status1);
		Assert.assertEquals("Team #0 (2): [2,2,2] => [2,2,2] (0.0)", status0.toString());
		status1 = new DamconStatus(0, 2, 2, 2, 2, 1, 1, 1, 0);
		status0.updateFrom(status1);
		Assert.assertEquals("Team #0 (2): [1,1,1] => [2,2,2] (0.0)", status0.toString());
	}
}
