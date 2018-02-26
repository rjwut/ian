package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class KeyCaptureTogglePacketTest extends AbstractPacketTester<KeyCaptureTogglePacket> {
	@Test
	public void test() {
		execute("core/KeyCaptureTogglePacket.txt", Origin.SERVER, 2);
	}

	@Test
	public void testConstruct() {
		Assert.assertTrue(new KeyCaptureTogglePacket(true).isEnabled());
		Assert.assertFalse(new KeyCaptureTogglePacket(false).isEnabled());
	}

	@Override
	protected void testPackets(List<KeyCaptureTogglePacket> packets) {
		Assert.assertTrue(packets.get(0).isEnabled());
		Assert.assertFalse(packets.get(1).isEnabled());
	}
}
