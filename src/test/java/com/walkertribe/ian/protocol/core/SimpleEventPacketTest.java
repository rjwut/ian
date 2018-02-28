package com.walkertribe.ian.protocol.core;

import org.junit.Test;

import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;
import com.walkertribe.ian.util.TestUtil;

public class SimpleEventPacketTest {
	@Test
	public void makeEclEmmaHappy() {
		TestUtil.coverPrivateConstructor(SubType.class);
	}
}
