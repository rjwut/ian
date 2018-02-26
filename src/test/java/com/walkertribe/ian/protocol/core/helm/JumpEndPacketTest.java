package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class JumpEndPacketTest extends AbstractPacketTester<JumpEndPacket> {
	@Test
	public void test() {
		execute("core/helm/JumpEndPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		new JumpEndPacket();
	}

	@Override
	protected void testPackets(List<JumpEndPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
