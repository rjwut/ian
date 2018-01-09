package com.walkertribe.ian.protocol.core.helm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class JumpBeginPacketTest extends AbstractPacketTester<JumpBeginPacket> {
	@Test
	public void test() {
		execute("core/helm/JumpBeginPacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		new JumpBeginPacket();
	}

	@Override
	protected void testPackets(List<JumpBeginPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
