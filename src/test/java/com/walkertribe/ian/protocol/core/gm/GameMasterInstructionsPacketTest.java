package com.walkertribe.ian.protocol.core.gm;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class GameMasterInstructionsPacketTest extends AbstractPacketTester<GameMasterInstructionsPacket> {
	@Test
	public void test() {
		execute("core/gm/GameMasterInstructionsPacket.txt", Origin.SERVER, 1);
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
		TestUtil.assertToStringEquals("Title", pkt.getTitle());
		TestUtil.assertToStringEquals("Content", pkt.getContent());
	}
}
