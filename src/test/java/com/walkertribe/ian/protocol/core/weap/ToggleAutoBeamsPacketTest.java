package com.walkertribe.ian.protocol.core.weap;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class ToggleAutoBeamsPacketTest extends AbstractPacketTester<ToggleAutoBeamsPacket> {
	@Test
	public void test() {
		execute("core/weap/ToggleAutoBeamsPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		new ToggleAutoBeamsPacket();
	}

	@Override
	protected void testPackets(List<ToggleAutoBeamsPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
