package com.walkertribe.ian.protocol.core.eng;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class EngResetCoolantPacketTest extends AbstractPacketTester<EngResetCoolantPacket> {
	@Test
	public void testParse() {
		execute("core/eng/EngResetCoolantPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		new EngResetCoolantPacket();
	}

	@Override
	protected void testPackets(List<EngResetCoolantPacket> packets) {
		Assert.assertNotNull(packets.get(0));
	}
}
