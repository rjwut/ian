package com.walkertribe.ian.protocol.core.setup;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.enums.ConsoleStatus;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.Artemis;

public class ConsoleStatusPacketTest extends AbstractPacketTester<ConsoleStatusPacket> {
	@Test
	public void test() {
		execute("core/setup/ConsoleStatusPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		ConsoleStatus[] statuses = buildConsoleStatusArray(Console.values().length);
		test(new ConsoleStatusPacket(1, statuses));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructShipIndexTooLow() {
		new ConsoleStatusPacket(-1, buildConsoleStatusArray(Console.values().length));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructShipIndexTooHigh() {
		new ConsoleStatusPacket(Artemis.SHIP_COUNT, buildConsoleStatusArray(Console.values().length));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullArray() {
		new ConsoleStatusPacket(1, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructArrayTooShort() {
		new ConsoleStatusPacket(1, buildConsoleStatusArray(Console.values().length - 1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructArrayTooLong() {
		new ConsoleStatusPacket(1, buildConsoleStatusArray(Console.values().length + 1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructArrayContainsNull() {
		ConsoleStatus[] statuses = buildConsoleStatusArray(Console.values().length);
		statuses[statuses.length - 1] = null;
		new ConsoleStatusPacket(1, statuses);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetNullArg() {
		ConsoleStatusPacket pkt = new ConsoleStatusPacket(1, buildConsoleStatusArray(Console.values().length));
		pkt.get(null);
	}

	@Override
	protected void testPackets(List<ConsoleStatusPacket> packets) {
		test(packets.get(0));
	}

	private void test(ConsoleStatusPacket pkt) {
		Assert.assertEquals(pkt.getShipIndex(), 1);
		Console[] consoleValues = Console.values();
		ConsoleStatus[] statusValues = ConsoleStatus.values();

		for (int i = 0; i < consoleValues.length; i++) {
			Assert.assertEquals(statusValues[i % statusValues.length], pkt.get(consoleValues[i]));
		}

		Assert.assertEquals(
				"[ConsoleStatusPacket] Ship #1\n\tMain screen: AVAILABLE\n\tHelm: YOURS\n\tWeapons: UNAVAILABLE\n\t" +
				"Engineering: AVAILABLE\n\tScience: YOURS\n\tCommunications: UNAVAILABLE\n\tSingle-seat craft: AVAILABLE\n\t" +
				"Data: YOURS\n\tObserver: UNAVAILABLE\n\tCaptain's map: AVAILABLE\n\tGame master: YOURS",
				pkt.toString()
		);
	}

	private ConsoleStatus[] buildConsoleStatusArray(int length) {
		ConsoleStatus[] statuses = new ConsoleStatus[length];
		ConsoleStatus[] statusValues = ConsoleStatus.values();

		for (int i = 0; i < length; i++) {
			statuses[i] = statusValues[i % statusValues.length];
		}

		return statuses;
	}
}
