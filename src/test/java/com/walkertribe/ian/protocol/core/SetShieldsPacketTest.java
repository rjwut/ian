package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.protocol.core.SetShieldsPacket.Action;
import com.walkertribe.ian.util.TestUtil;

public class SetShieldsPacketTest extends AbstractPacketTester<SetShieldsPacket> {
	@Test
	public void test() {
		execute("core/SetShieldsPacket.txt", Origin.CLIENT, 3);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(Action.TOGGLE, new SetShieldsPacket(Action.TOGGLE).getAction());
		Assert.assertEquals(Action.UP, new SetShieldsPacket(Action.UP).getAction());
		Assert.assertEquals(Action.DOWN, new SetShieldsPacket(Action.DOWN).getAction());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNull() {
		new SetShieldsPacket((Action) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidSubtype() {
		Action.fromSubType((byte) 0);
	}

	@Test
	public void makeEclEmmaHappy() {
		TestUtil.coverEnumValueOf(Action.class);
	}

	@Override
	protected void testPackets(List<SetShieldsPacket> packets) {
		Assert.assertEquals(Action.TOGGLE, packets.get(0).getAction());
		Assert.assertEquals(Action.UP, packets.get(1).getAction());
		Assert.assertEquals(Action.DOWN, packets.get(2).getAction());
	}
}
