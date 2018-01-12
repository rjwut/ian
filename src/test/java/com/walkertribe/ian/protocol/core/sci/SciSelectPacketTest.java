package com.walkertribe.ian.protocol.core.sci;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.ArtemisCreature;

public class SciSelectPacketTest extends AbstractPacketTester<SciSelectPacket> {
	@Test
	public void test() {
		execute("core/sci/SciSelectPacket.txt", ConnectionType.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		test(
				new SciSelectPacket(new ArtemisCreature(47)),
				new SciSelectPacket(null)
		);
	}

	@Override
	protected void testPackets(List<SciSelectPacket> packets) {
		test(packets.get(0), packets.get(1));
	}

	private void test(SciSelectPacket pkt0, SciSelectPacket pkt1) {
		Assert.assertEquals(47, pkt0.getTargetId());
		Assert.assertEquals(1, pkt1.getTargetId());
	}
}
