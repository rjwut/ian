package com.walkertribe.ian.protocol.core.sci;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.ArtemisCreature;

public class SciScanPacketTest extends AbstractPacketTester<SciScanPacket> {
	@Test
	public void test() {
		execute("core/sci/SciScanPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(47, new SciScanPacket(new ArtemisCreature(47)).getTargetId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNull() {
		new SciScanPacket((ArtemisCreature) null);
	}

	@Override
	protected void testPackets(List<SciScanPacket> packets) {
		Assert.assertEquals(47, packets.get(0).getTargetId());
	}
}
