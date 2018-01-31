package com.walkertribe.ian.protocol.core.gm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.JamCrc;

public class GameMasterButtonClickPacketTest extends AbstractPacketTester<GameMasterButtonClickPacket> {
	private static final int HASH = JamCrc.compute("Test");

	@Test
	public void test() {
		execute("core/gm/GameMasterButtonClickPacket.txt", ConnectionType.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		GameMasterButtonClickPacket pkt = new GameMasterButtonClickPacket(HASH);
		Assert.assertEquals(HASH, pkt.getHash());
		pkt = new GameMasterButtonClickPacket("Test");
		Assert.assertEquals(HASH, pkt.getHash());
	}

	@Override
	protected void testPackets(List<GameMasterButtonClickPacket> packets) {
		Assert.assertEquals(HASH, packets.get(0).getHash());
	}
}
