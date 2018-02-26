package com.walkertribe.ian.protocol.core.eng;

import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class EngSetAutoDamconPacketTest extends AbstractPacketTester<EngSetAutoDamconPacket> {
	@Test
	public void test() {
		execute("core/eng/EngSetAutoDamconPacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		Assert.assertTrue(new EngSetAutoDamconPacket(true).isAutonomous());
		Assert.assertFalse(new EngSetAutoDamconPacket(false).isAutonomous());
	}

	@Override
	protected void testPackets(List<EngSetAutoDamconPacket> packets) {
		EngSetAutoDamconPacket pkt = packets.get(0);
		Assert.assertFalse(pkt.isAutonomous());
		pkt = packets.get(1);
		Assert.assertTrue(pkt.isAutonomous());
	}
}