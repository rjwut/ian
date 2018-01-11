package com.walkertribe.ian.protocol.core.fighter;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class FighterLaunchPacketTest extends AbstractPacketTester<FighterLaunchPacket> {
	@Test
	public void test() {
		execute("core/fighter/FighterLaunchPacket.txt", ConnectionType.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(1, new FighterLaunchPacket(1).getObjectId());
	}

	@Override
	protected void testPackets(List<FighterLaunchPacket> packets) {
		Assert.assertEquals(1, packets.get(0).getObjectId());
	}
}
