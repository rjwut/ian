package com.walkertribe.ian.protocol.core.gm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.ArtemisCreature;

public class GameMasterSelectObjectPacketTest extends AbstractPacketTester<GameMasterSelectObjectPacket> {
	@Test
	public void test() {
		execute("core/gm/GameMasterSelectObjectPacket.txt", ConnectionType.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		test(
				new GameMasterSelectObjectPacket(null),
				new GameMasterSelectObjectPacket(new ArtemisCreature(47))
		);
	}

	@Override
	protected void testPackets(List<GameMasterSelectObjectPacket> packets) {
		test(packets.get(0), packets.get(1));
	}

	private void test(GameMasterSelectObjectPacket pkt0, GameMasterSelectObjectPacket pkt1) {
		Assert.assertEquals(1, pkt0.getTargetId());
		Assert.assertEquals(47, pkt1.getTargetId());
	}
}
