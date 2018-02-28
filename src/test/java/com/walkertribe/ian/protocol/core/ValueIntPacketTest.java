package com.walkertribe.ian.protocol.core;

import org.junit.Test;

import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.util.TestUtil;

public class ValueIntPacketTest {
	@Test
	public void makeEclEmmaHappy() {
		TestUtil.coverPrivateConstructor(SubType.class);
	}
}
