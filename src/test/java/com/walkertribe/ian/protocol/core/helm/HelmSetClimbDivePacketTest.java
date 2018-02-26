package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class HelmSetClimbDivePacketTest extends AbstractPacketTester<HelmSetClimbDivePacket> {
	@Test
	public void test() {
		execute("core/helm/HelmSetClimbDivePacket.txt", Origin.CLIENT, 3);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(-1.0f, new HelmSetClimbDivePacket(-1.0f).getPitch(), EPSILON);
		Assert.assertEquals(0.0f, new HelmSetClimbDivePacket(0.0f).getPitch(), EPSILON);
		Assert.assertEquals(1.0f, new HelmSetClimbDivePacket(1.0f).getPitch(), EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructPastMaxClimb() {
		new HelmSetClimbDivePacket(-1.01f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructPastMaxDive() {
		new HelmSetClimbDivePacket(1.01f);
	}

	@Override
	protected void testPackets(List<HelmSetClimbDivePacket> packets) {
		HelmSetClimbDivePacket pkt = packets.get(0);
		Assert.assertEquals(0.0f, pkt.getPitch(), EPSILON);
		pkt = packets.get(1);
		Assert.assertEquals(-1.0f, pkt.getPitch(), EPSILON);
		pkt = packets.get(2);
		Assert.assertEquals(0.3f, pkt.getPitch(), EPSILON);
	}
}