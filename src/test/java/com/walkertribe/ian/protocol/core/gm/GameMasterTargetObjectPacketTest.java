package com.walkertribe.ian.protocol.core.gm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.ArtemisCreature;

public class GameMasterTargetObjectPacketTest extends AbstractPacketTester<GameMasterTargetObjectPacket> {
	@Test
	public void test() {
		execute("core/gm/GameMasterTargetObjectPacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		test(
				new GameMasterTargetObjectPacket((ArtemisCreature) null),
				new GameMasterTargetObjectPacket(new ArtemisCreature(47))
		);
	}

	@Override
	protected void testPackets(List<GameMasterTargetObjectPacket> packets) {
		test(packets.get(0), packets.get(1));
	}

	private void test(GameMasterTargetObjectPacket pkt0, GameMasterTargetObjectPacket pkt1) {
		Assert.assertEquals(1, pkt0.getTargetId());
		Assert.assertEquals(47, pkt1.getTargetId());
	}
}
