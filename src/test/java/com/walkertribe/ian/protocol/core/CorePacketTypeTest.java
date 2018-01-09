package com.walkertribe.ian.protocol.core;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.util.TestUtil;

public class CorePacketTypeTest {
	@Test
	public void testGetInternalName() {
		Assert.assertEquals("attack", CorePacketType.ATTACK.getInternalName());
		Assert.assertEquals("beamRequest", CorePacketType.BEAM_REQUEST.getInternalName());
	}

	@Test
	public void makeEclEmmaHappy() {
		TestUtil.coverEnumValueOf(CorePacketType.class);
		TestUtil.coverEnumValueOf(ValueIntPacket.SubType.class);
		TestUtil.coverEnumValueOf(SimpleEventPacket.SubType.class);
	}
}
