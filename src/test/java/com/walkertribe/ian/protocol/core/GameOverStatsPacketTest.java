package com.walkertribe.ian.protocol.core;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class GameOverStatsPacketTest extends AbstractPacketTester<GameOverStatsPacket> {
	private static final Map<String, Integer> ROWS = new LinkedHashMap<String, Integer>();

	static {
		ROWS.put("Enemies destroyed", 38);
		ROWS.put("Enemies surrendered", 0);
		ROWS.put("Enemies survived", 16);
	}

	@Test
	public void test() {
		execute("core/GameOverStatsPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		GameOverStatsPacket pkt = new GameOverStatsPacket((byte) 0);

		for (Map.Entry<String, Integer> entry : ROWS.entrySet()) {
			pkt.addRow(entry.getKey(), entry.getValue().intValue());
		}

		Assert.assertEquals(0, pkt.getColumnIndex());
		Assert.assertEquals(ROWS.size(), pkt.getRowCount());

		for (GameOverStatsPacket.Row row : pkt) {
			Assert.assertEquals(row.getValue(), ROWS.get(row.getLabel()).intValue());
		}

		// no rows
		pkt = new GameOverStatsPacket((byte) 1);
		Assert.assertEquals(1, pkt.getColumnIndex());
		Assert.assertEquals(0, pkt.getRowCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullLabel() {
		new GameOverStatsPacket((byte) 0).addRow(null, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructBlankLabel() {
		new GameOverStatsPacket((byte) 1).addRow("", 0);
	}

	@Override
	protected void testPackets(List<GameOverStatsPacket> packets) {
		GameOverStatsPacket pkt = packets.get(0);
		Assert.assertEquals(1, pkt.getColumnIndex());
		Iterator<GameOverStatsPacket.Row> iter = pkt.iterator();
		GameOverStatsPacket.Row row = iter.next();
		TestUtil.assertToStringEquals("Hi!", row.getLabel());
		Assert.assertEquals(7, row.getValue());
		row = iter.next();
		TestUtil.assertToStringEquals("Bye!", row.getLabel());
		Assert.assertEquals(4, row.getValue());
		Assert.assertFalse(iter.hasNext());
	}
}
