package com.walkertribe.ian.protocol.core.gm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class GameMasterSelectLocationPacketTest extends AbstractPacketTester<GameMasterSelectLocationPacket> {
	@Test
	public void test() {
		execute("core/gm/GameMasterSelectLocationPacket.txt", ConnectionType.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		test(new GameMasterSelectLocationPacket(1.0f, 2.0f));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullSender() {
		new GameMasterMessagePacket(null, "Message");
	}

	@Override
	protected void testPackets(List<GameMasterSelectLocationPacket> packets) {
		test(packets.get(0));
	}

	private void test(GameMasterSelectLocationPacket pkt) {
		Assert.assertEquals(1.0f, pkt.getX(), EPSILON);
		Assert.assertEquals(2.0f, pkt.getZ(), EPSILON);
	}
}
