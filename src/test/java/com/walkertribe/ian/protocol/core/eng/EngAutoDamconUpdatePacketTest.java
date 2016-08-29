package com.walkertribe.ian.protocol.core.eng;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class EngAutoDamconUpdatePacketTest extends AbstractPacketTester<EngAutoDamconUpdatePacket> {
	@Test
	public void test() {
		execute("core/eng/EngAutoDamconUpdatePacket.txt", ConnectionType.SERVER, 2);
	}

	@Override
	protected void testPackets(List<EngAutoDamconUpdatePacket> packets) {
		EngAutoDamconUpdatePacket pkt = packets.get(0);
		Assert.assertFalse(pkt.isOn());
		pkt = packets.get(1);
		Assert.assertTrue(pkt.isOn());
	}
}