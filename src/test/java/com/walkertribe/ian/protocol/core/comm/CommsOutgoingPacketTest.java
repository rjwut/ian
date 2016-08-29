package com.walkertribe.ian.protocol.core.comm;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.walkertribe.ian.enums.BaseMessage;
import com.walkertribe.ian.enums.CommsRecipientType;
import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.EnemyMessage;
import com.walkertribe.ian.enums.OtherMessage;
import com.walkertribe.ian.enums.PlayerMessage;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.vesseldata.ClasspathResolver;
import com.walkertribe.ian.vesseldata.VesselData;
import com.walkertribe.ian.vesseldata.VesselDataTest;
import com.walkertribe.ian.world.ArtemisBase;
import com.walkertribe.ian.world.ArtemisNebula;
import com.walkertribe.ian.world.ArtemisNpc;

public class CommsOutgoingPacketTest extends AbstractPacketTester<CommsOutgoingPacket> {
	@BeforeClass
	public static void beforeClass() {
		VesselData.setPathResolver(new ClasspathResolver(VesselDataTest.class));
	}

	@AfterClass
	public static void afterClass() {
		VesselData.setPathResolver(null);
	}

	@Test
	public void testParse() {
		execute("core/comm/CommsOutgoingPacket.txt", ConnectionType.CLIENT, 4);
	}

	@Test
	public void testConstruct() {
		new CommsOutgoingPacket(new ArtemisBase(1), BaseMessage.BUILD_EMPS);
		ArtemisNpc npc = new ArtemisNpc(1);
		npc.setVessel(VesselData.get().getVessel(1500));
		new CommsOutgoingPacket(npc, OtherMessage.GO_DEFEND, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRequiresRecipient() {
		new CommsOutgoingPacket(null, BaseMessage.BUILD_EMPS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRequiresMessage() {
		new CommsOutgoingPacket(new ArtemisBase(1), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidRecipient() {
		new CommsOutgoingPacket(new ArtemisNebula(1), BaseMessage.BUILD_EMPS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIncompatibleMessage() {
		new CommsOutgoingPacket(new ArtemisBase(1), EnemyMessage.WILL_YOU_SURRENDER);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRequiresArgument() {
		ArtemisNpc npc = new ArtemisNpc(1);
		npc.setVessel(VesselData.get().getVessel(1500));
		new CommsOutgoingPacket(npc, OtherMessage.GO_DEFEND);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDoesNotAcceptArgument() {
		new CommsOutgoingPacket(new ArtemisBase(1), BaseMessage.BUILD_EMPS, 2);
	}

	@Override
	protected void testPackets(List<CommsOutgoingPacket> packets) {
		CommsOutgoingPacket pkt = packets.get(0);
		Assert.assertEquals(CommsRecipientType.PLAYER, pkt.getRecipientType());
		Assert.assertEquals(0, pkt.getRecipientId());
		Assert.assertEquals(PlayerMessage.DIE, pkt.getMessage());
		Assert.assertEquals(CommsOutgoingPacket.NO_ARG, pkt.getArgument());

		pkt = packets.get(1);
		Assert.assertEquals(CommsRecipientType.ENEMY, pkt.getRecipientType());
		Assert.assertEquals(1, pkt.getRecipientId());
		Assert.assertEquals(EnemyMessage.WILL_YOU_SURRENDER, pkt.getMessage());
		Assert.assertEquals(CommsOutgoingPacket.NO_ARG, pkt.getArgument());

		pkt = packets.get(2);
		Assert.assertEquals(CommsRecipientType.BASE, pkt.getRecipientType());
		Assert.assertEquals(2, pkt.getRecipientId());
		Assert.assertEquals(BaseMessage.BUILD_NUKES, pkt.getMessage());
		Assert.assertEquals(CommsOutgoingPacket.NO_ARG, pkt.getArgument());

		pkt = packets.get(3);
		Assert.assertEquals(CommsRecipientType.OTHER, pkt.getRecipientType());
		Assert.assertEquals(3, pkt.getRecipientId());
		Assert.assertEquals(OtherMessage.GO_DEFEND, pkt.getMessage());
		Assert.assertEquals(47, pkt.getArgument());
	}
}