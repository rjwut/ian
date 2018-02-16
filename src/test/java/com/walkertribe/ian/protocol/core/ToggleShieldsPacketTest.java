package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.protocol.core.SetShieldsPacket.Action;

public class ToggleShieldsPacketTest extends AbstractPacketTester<SetShieldsPacket> {
	@Test
	public void test() {
		execute("core/SetShieldsPacket.txt", ConnectionType.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		new SetShieldsPacket(Action.TOGGLE);
	}

	@Override
	protected void testPackets(List<SetShieldsPacket> packets) {
		// TODO Expand to cover UP and DOWN actions
		Assert.assertNotNull(packets.get(0));
	}
}
