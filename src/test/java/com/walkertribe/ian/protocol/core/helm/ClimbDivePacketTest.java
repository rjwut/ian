package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class ClimbDivePacketTest extends AbstractPacketTester<ClimbDivePacket> {
	@Test
	public void test() {
		execute("core/helm/ClimbDivePacket.txt", Origin.CLIENT, 2);
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