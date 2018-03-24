package com.walkertribe.ian.protocol.core.gm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class GameMasterTargetLocationPacketTest extends AbstractPacketTester<GameMasterTargetLocationPacket> {
	@Test
	public void test() {
		execute("core/gm/GameMasterTargetLocationPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		test(new GameMasterTargetLocationPacket(1.0f, 2.0f));
	}

	@Override
	protected void testPackets(List<GameMasterTargetLocationPacket> packets) {
		test(packets.get(0));
	}

	private void test(GameMasterTargetLocationPacket pkt) {
		Assert.assertEquals(1.0f, pkt.getX(), EPSILON);
		Assert.assertEquals(2.0f, pkt.getZ(), EPSILON);
	}
}
