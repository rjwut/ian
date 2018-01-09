package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.MainScreenView;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class SetMainScreenPacketTest extends AbstractPacketTester<SetMainScreenPacket> {
	@Test
	public void test() {
		execute("core/SetMainScreenPacket.txt", ConnectionType.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(MainScreenView.TACTICAL, new SetMainScreenPacket(MainScreenView.TACTICAL).getView());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNull() {
		new SetMainScreenPacket(null);
	}

	@Override
	protected void testPackets(List<SetMainScreenPacket> packets) {
		Assert.assertEquals(MainScreenView.LONG_RANGE, packets.get(0).getView());
	}
}