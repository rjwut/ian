package com.walkertribe.ian.protocol.core.weap;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.world.ArtemisCreature;

public class WeaponsTargetPacketTest extends AbstractPacketTester<WeaponsTargetPacket> {
	@Test
	public void test() {
		execute("core/weap/WeaponsTargetPacket.txt", Origin.CLIENT, 2);
	}

	@Test
	public void testConstruct() {
		test(
				new WeaponsTargetPacket(new ArtemisCreature(47)),
				new WeaponsTargetPacket((ArtemisCreature) null)
		);
	}

	@Override
	protected void testPackets(List<WeaponsTargetPacket> packets) {
		test(packets.get(0), packets.get(1));
	}

	private void test(WeaponsTargetPacket pkt0, WeaponsTargetPacket pkt1) {
		Assert.assertEquals(47, pkt0.getTargetId());
		Assert.assertEquals(1, pkt1.getTargetId());
	}
}
