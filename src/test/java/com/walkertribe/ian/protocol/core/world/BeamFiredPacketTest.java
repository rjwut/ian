package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.TargetingMode;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;
import com.walkertribe.ian.world.ArtemisNpc;
import com.walkertribe.ian.world.ArtemisPlayer;

public class BeamFiredPacketTest extends AbstractPacketTester<BeamFiredPacket> {
	@Test
	public void test() {
		execute("core/world/BeamFiredPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		BeamFiredPacket pkt = new BeamFiredPacket(0);
		pkt.setBeamPortIndex(1);
		pkt.setOrigin(new ArtemisNpc(2));
		pkt.setTarget(new ArtemisPlayer(3));
		pkt.setImpactX(1);
		pkt.setImpactY(2);
		pkt.setImpactZ(3);
		pkt.setTargetingMode(TargetingMode.MANUAL);
		test(pkt);

		pkt = new BeamFiredPacket(0);
		pkt.setBeamPortIndex(0);
		pkt.setOrigin(new ArtemisPlayer(2));
		pkt.setTarget(new ArtemisNpc(3));
		pkt.setImpactX(1);
		pkt.setImpactY(2);
		pkt.setImpactZ(3);
		pkt.setTargetingMode(TargetingMode.AUTO);
	}

	@Override
	protected void testPackets(List<BeamFiredPacket> packets) {
		test(packets.get(0));
	}

	private void test(BeamFiredPacket pkt) {
		Assert.assertEquals(0, pkt.getBeamId());
		Assert.assertEquals(1, pkt.getBeamPortIndex());
		Assert.assertEquals(ObjectType.NPC_SHIP, pkt.getOriginObjectType());
		Assert.assertEquals(ObjectType.PLAYER_SHIP, pkt.getTargetObjectType());
		Assert.assertEquals(2, pkt.getOriginId());
		Assert.assertEquals(3, pkt.getTargetId());
		Assert.assertEquals(1, pkt.getImpactX(), TestUtil.EPSILON);
		Assert.assertEquals(2, pkt.getImpactY(), TestUtil.EPSILON);
		Assert.assertEquals(3, pkt.getImpactZ(), TestUtil.EPSILON);
		Assert.assertEquals(TargetingMode.MANUAL, pkt.getTargetingMode());
	}
}
