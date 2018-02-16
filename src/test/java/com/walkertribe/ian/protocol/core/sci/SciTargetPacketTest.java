package com.walkertribe.ian.protocol.core.sci;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.ArtemisCreature;

public class SciTargetPacketTest extends AbstractPacketTester<SciTargetPacket> {
	@Test
	public void test() {
		execute("core/sci/SciTargetPacket.txt", ConnectionType.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		test(
				new SciTargetPacket(new ArtemisCreature(47)),
				new SciTargetPacket(null)
		);
	}

	@Override
	protected void testPackets(List<SciTargetPacket> packets) {
		test(packets.get(0), packets.get(1));
	}

	private void test(SciTargetPacket pkt0, SciTargetPacket pkt1) {
		Assert.assertEquals(47, pkt0.getTargetId());
		Assert.assertEquals(1, pkt1.getTargetId());
	}
}
