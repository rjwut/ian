package com.walkertribe.ian.protocol.core.gm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.protocol.core.ButtonClickPacket;
import com.walkertribe.ian.util.JamCrc;

public class GameMasterButtonClickPacketTest extends AbstractPacketTester<ButtonClickPacket> {
	private static final int HASH = JamCrc.compute("Test");

	@Test
	public void test() {
		execute("core/gm/GameMasterButtonClickPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		ButtonClickPacket pkt = new ButtonClickPacket(HASH);
		Assert.assertEquals(HASH, pkt.getHash());
		pkt = new ButtonClickPacket("Test");
		Assert.assertEquals(HASH, pkt.getHash());
	}

	@Override
	protected void testPackets(List<ButtonClickPacket> packets) {
		Assert.assertEquals(HASH, packets.get(0).getHash());
	}
}
