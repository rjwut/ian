package com.walkertribe.ian.protocol.core.gm;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.protocol.core.gm.GameMasterButtonPacket.Action;
import com.walkertribe.ian.util.JamCrc;
import com.walkertribe.ian.util.TestUtil;

public class GameMasterButtonPacketTest extends AbstractPacketTester<GameMasterButtonPacket> {
	private static final int HASH = JamCrc.compute("Test");

	@Test
	public void test() {
		execute("core/gm/GameMasterButtonPacket.txt", Origin.SERVER, 4);
	}

	@Test
	public void testConstruct() {
		GameMasterButtonPacket pkt1 = new GameMasterButtonPacket(Action.CREATE, "Test");
		GameMasterButtonPacket pkt2 = new GameMasterButtonPacket(Action.CREATE, "Test");
		pkt2.setRect(0, 0, 100, 30);
		GameMasterButtonPacket pkt3 = new GameMasterButtonPacket(Action.REMOVE, "Test");
		GameMasterButtonPacket pkt4 = new GameMasterButtonPacket(Action.REMOVE, null);
		test(pkt1, pkt2, pkt3, pkt4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullAction() {
		new GameMasterButtonPacket(null, "Test");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullLabel() {
		new GameMasterButtonPacket(Action.CREATE, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructBlankCreateLabel() {
		new GameMasterButtonPacket(Action.CREATE, "");
	}

	@Test(expected = IllegalStateException.class)
	public void testPositionRemoval() {
		new GameMasterButtonPacket(Action.REMOVE, "Test").setRect(0, 0, 100, 30);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeX() {
		new GameMasterButtonPacket(Action.CREATE, "Test").setRect(-1, 0, 100, 30);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeY() {
		new GameMasterButtonPacket(Action.CREATE, "Test").setRect(0, -1, 100, 30);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testZeroWidth() {
		new GameMasterButtonPacket(Action.CREATE, "Test").setRect(0, 0, 0, 30);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testZeroHeight() {
		new GameMasterButtonPacket(Action.CREATE, "Test").setRect(0, 0, 100, 0);
	}

	@Override
	protected void testPackets(List<GameMasterButtonPacket> packets) {
		test(packets.get(0), packets.get(1), packets.get(2), packets.get(3));
	}

	private void test(GameMasterButtonPacket pkt1,
			GameMasterButtonPacket pkt2,
			GameMasterButtonPacket pkt3,
			GameMasterButtonPacket pkt4) {
		Assert.assertEquals(Action.CREATE, pkt1.getAction());
		TestUtil.assertToStringEquals("Test", pkt1.getLabel());
		Assert.assertFalse(pkt1.isPositioned());
		Assert.assertFalse(pkt1.isRemoveAll());
		Assert.assertEquals(-1, pkt1.getHeight());
		Assert.assertEquals(-1, pkt1.getWidth());
		Assert.assertEquals(-1, pkt1.getX());
		Assert.assertEquals(-1, pkt1.getY());
		Assert.assertEquals(HASH, pkt1.buildClickPacket().getHash());
		Assert.assertEquals("[GameMasterButtonPacket] CREATE Test", pkt1.toString());
		Assert.assertEquals(Action.CREATE, pkt2.getAction());
		TestUtil.assertToStringEquals("Test", pkt2.getLabel());
		Assert.assertTrue(pkt2.isPositioned());
		Assert.assertFalse(pkt2.isRemoveAll());
		Assert.assertEquals(30, pkt2.getHeight());
		Assert.assertEquals(100, pkt2.getWidth());
		Assert.assertEquals(0, pkt2.getX());
		Assert.assertEquals(0, pkt2.getY());
		Assert.assertEquals(HASH, pkt2.buildClickPacket().getHash());
		Assert.assertEquals("[GameMasterButtonPacket] CREATE Test (0,0 100x30)", pkt2.toString());
		Assert.assertEquals(Action.REMOVE, pkt3.getAction());
		TestUtil.assertToStringEquals("Test", pkt3.getLabel());
		Assert.assertFalse(pkt3.isPositioned());
		Assert.assertFalse(pkt3.isRemoveAll());
		Assert.assertEquals(-1, pkt3.getHeight());
		Assert.assertEquals(-1, pkt3.getWidth());
		Assert.assertEquals(-1, pkt3.getX());
		Assert.assertEquals(-1, pkt3.getY());
		Assert.assertNull(pkt3.buildClickPacket());
		Assert.assertEquals("[GameMasterButtonPacket] REMOVE Test", pkt3.toString());
		Assert.assertEquals(Action.REMOVE, pkt4.getAction());
		Assert.assertNull(pkt4.getLabel());
		Assert.assertFalse(pkt4.isPositioned());
		Assert.assertTrue(pkt4.isRemoveAll());
		Assert.assertEquals(-1, pkt4.getHeight());
		Assert.assertEquals(-1, pkt4.getWidth());
		Assert.assertEquals(-1, pkt4.getX());
		Assert.assertEquals(-1, pkt4.getY());
		Assert.assertNull(pkt4.buildClickPacket());
		Assert.assertEquals("[GameMasterButtonPacket] REMOVE ALL", pkt4.toString());
	}

	@Test
	public void makeEclEmmaHappy() {
		TestUtil.coverEnumValueOf(GameMasterButtonPacket.Action.class);
	}
}
