package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.ArtemisCreature;

public class CaptainSelectPacketTest extends AbstractPacketTester<CaptainSelectPacket> {
	@Test
	public void test() {
		execute("core/CaptainSelectPacket.txt", ConnectionType.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(1047, new CaptainSelectPacket(new ArtemisCreature(1047)).getTargetId());
		Assert.assertEquals(1, new CaptainSelectPacket(null).getTargetId());
	}

	@Override
	protected void testPackets(List<CaptainSelectPacket> packets) {
		Assert.assertEquals(1047, packets.get(0).getTargetId());
		Assert.assertEquals(1, packets.get(1).getTargetId());
	}

}
