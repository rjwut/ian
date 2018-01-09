package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.Perspective;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class PerspectivePacketTest extends AbstractPacketTester<PerspectivePacket> {
	@Test
	public void test() {
		execute("core/PerspectivePacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(Perspective.THIRD_PERSON, new PerspectivePacket(Perspective.THIRD_PERSON).getPerspective());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNull() {
		new PerspectivePacket(null);
	}

	@Override
	protected void testPackets(List<PerspectivePacket> packets) {
		Assert.assertEquals(Perspective.THIRD_PERSON, packets.get(0).getPerspective());
	}
}