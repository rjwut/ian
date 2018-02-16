package com.walkertribe.ian.protocol.core.singleseat;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.protocol.core.singleseat.BayStatusPacket;
import com.walkertribe.ian.protocol.core.singleseat.BayStatusPacket.Bay;
import com.walkertribe.ian.util.TestUtil;

public class BayStatusPacketTest extends AbstractPacketTester<BayStatusPacket> {
	@Test
	public void test() {
		execute("core/fighter/BayStatusPacket.txt", ConnectionType.SERVER, 3);
	}

	@Test
	public void testConstruct() {
		BayStatusPacket pkt = new BayStatusPacket();
		pkt.addBay(new Bay(1, "Fighter 1", "Class 1", 257));
		pkt.addBay(new Bay(2, "Fighter 2", "Class 2", 0));
		test(pkt, 2);
	}

	@Override
	protected void testPackets(List<BayStatusPacket> packets) {
		for (int i = 0; i < 2; i++) {
			test(packets.get(i), i);
		}
	}

	private void test(BayStatusPacket pkt, int bayCount) {
		Assert.assertEquals(bayCount, pkt.getBayCount());
		int id = 1;

		for (Bay bay : pkt) {
			Assert.assertEquals(id, bay.getId());
			TestUtil.assertToStringEquals("Fighter " + id, bay.getName());
			TestUtil.assertToStringEquals("Class " + id, bay.getClassName());
			Assert.assertEquals(id++ == 1 ? 257 : 0, bay.getRefitTime());
		}
	}

	@Test
	public void testBay() {
		BayStatusPacket.Bay bay = new Bay(1, "Test", "Class", 200);
		Assert.assertEquals(1, bay.getId());
		Assert.assertEquals("Test", bay.getName());
		Assert.assertEquals("Class", bay.getClassName());
		Assert.assertEquals(200, bay.getRefitTime());
		Assert.assertEquals("#1: Test (Class): 200", bay.toString());
		TestUtil.testEqualsAndHashCode(bay,
				new Bay(1, "Test", "Class", 200),
				new Bay(2, "Test", "Class", 200)
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBayZeroId() {
		new Bay(0, "Name", "Class", 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBayNullName() {
		new Bay(1, null, "Class", 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBayEmptyName() {
		new Bay(1, "", "Class", 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBayNullClass() {
		new Bay(1, "Name", null, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBayEmptyClass() {
		new Bay(1, "Name", "", 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBayNegativeRefit() {
		new Bay(1, "Name", "Class", -1);
	}
}
