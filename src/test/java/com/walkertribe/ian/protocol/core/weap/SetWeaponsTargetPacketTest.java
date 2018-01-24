package com.walkertribe.ian.protocol.core.weap;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.ArtemisCreature;

public class SetWeaponsTargetPacketTest extends AbstractPacketTester<SetWeaponsTargetPacket> {
	@Test
	public void test() {
		execute("core/weap/SetWeaponsTargetPacket.txt", ConnectionType.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		test(
				new SetWeaponsTargetPacket(new ArtemisCreature(47)),
				new SetWeaponsTargetPacket(null)
		);
	}

	@Override
	protected void testPackets(List<SetWeaponsTargetPacket> packets) {
		test(packets.get(0), packets.get(1));
	}

	private void test(SetWeaponsTargetPacket pkt0, SetWeaponsTargetPacket pkt1) {
		Assert.assertEquals(47, pkt0.getTargetId());
		Assert.assertEquals(1, pkt1.getTargetId());
	}
}
