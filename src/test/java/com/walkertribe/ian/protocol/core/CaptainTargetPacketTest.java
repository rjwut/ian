package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.ArtemisCreature;

public class CaptainTargetPacketTest extends AbstractPacketTester<CaptainTargetPacket> {
	@Test
	public void test() {
		execute("core/CaptainTargetPacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(1047, new CaptainTargetPacket(new ArtemisCreature(1047)).getTargetId());
		Assert.assertEquals(1, new CaptainTargetPacket((ArtemisCreature) null).getTargetId());
	}

	@Override
	protected void testPackets(List<CaptainTargetPacket> packets) {
		Assert.assertEquals(1047, packets.get(0).getTargetId());
		Assert.assertEquals(1, packets.get(1).getTargetId());
	}

}
