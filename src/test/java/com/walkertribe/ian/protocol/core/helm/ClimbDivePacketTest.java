package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class ClimbDivePacketTest extends AbstractPacketTester<ClimbDivePacket> {
	@Test
	public void test() {
		execute("core/helm/ClimbDivePacket.txt", ConnectionType.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		Assert.assertTrue(new ClimbDivePacket(true).isUp());
		Assert.assertFalse(new ClimbDivePacket(false).isUp());
	}

	@Override
	protected void testPackets(List<ClimbDivePacket> packets) {
		Assert.assertTrue(packets.get(0).isUp());
		Assert.assertFalse(packets.get(1).isUp());
	}
}