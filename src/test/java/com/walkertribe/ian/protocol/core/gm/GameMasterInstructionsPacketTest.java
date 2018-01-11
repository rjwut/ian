package com.walkertribe.ian.protocol.core.gm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class GameMasterInstructionsPacketTest extends AbstractPacketTester<GameMasterInstructionsPacket> {
	@Test
	public void test() {
		execute("core/gm/GameMasterInstructionsPacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		test(new GameMasterInstructionsPacket("Title", "Content"));
	}

	@Override
	protected void testPackets(List<GameMasterInstructionsPacket> packets) {
		test(packets.get(0));
	}
	
	private void test(GameMasterInstructionsPacket pkt) {
		Assert.assertEquals("Title", pkt.getTitle());
		Assert.assertEquals("Content", pkt.getContent());
	}
}
