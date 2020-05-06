package com.walkertribe.ian.protocol.core.comm;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.walkertribe.ian.ClasspathResolver;
import com.walkertribe.ian.Context;
import com.walkertribe.ian.DefaultContext;
import com.walkertribe.ian.enums.BaseMessage;
import com.walkertribe.ian.enums.CommsRecipientType;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.EnemyMessage;
import com.walkertribe.ian.enums.OtherMessage;
import com.walkertribe.ian.enums.PlayerMessage;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.vesseldata.VesselDataTest;
import com.walkertribe.ian.world.ArtemisBase;
import com.walkertribe.ian.world.ArtemisNebula;
import com.walkertribe.ian.world.ArtemisNpc;
import com.walkertribe.ian.world.ArtemisPlayer;

public class CommsOutgoingPacketTest extends AbstractPacketTester<CommsOutgoingPacket> {
	private static Context ctx;

	@BeforeClass
	public static void beforeClass() {
		ctx = new DefaultContext(new ClasspathResolver(VesselDataTest.class));
	}

	@AfterClass
	public static void afterClass() {
		ctx = null;
	}

	@Test
	public void testParse() {
		execute("core/comm/CommsOutgoingPacket.txt", Origin.CLIENT, 4);
	}

	@Test
	public void testConstruct() {
	    ArtemisPlayer player = new ArtemisPlayer(0);
		new CommsOutgoingPacket(player, new ArtemisBase(1), BaseMessage.BUILD_EMPS, ctx);
		ArtemisNpc npc = new ArtemisNpc(1);
		npc.setVessel(ctx.getVesselData().getVessel(1500));
		new CommsOutgoingPacket(player, npc, OtherMessage.GO_DEFEND, 2, ctx);
	}

    @Test(expected = IllegalArgumentException.class)
    public void testRequiresSender() {
        new CommsOutgoingPacket(null, new ArtemisBase(0), BaseMessage.BUILD_EMPS, ctx);
    }

	@Test(expected = IllegalArgumentException.class)
	public void testRequiresRecipient() {
		new CommsOutgoingPacket(new ArtemisPlayer(1), null, BaseMessage.BUILD_EMPS, ctx);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRequiresMessage() {
		new CommsOutgoingPacket(new ArtemisPlayer(0), new ArtemisBase(1), null, ctx);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidRecipient() {
		new CommsOutgoingPacket(new ArtemisPlayer(0), new ArtemisNebula(1), BaseMessage.BUILD_EMPS, ctx);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIncompatibleMessage() {
		new CommsOutgoingPacket(new ArtemisPlayer(0), new ArtemisBase(1), EnemyMessage.WILL_YOU_SURRENDER, ctx);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRequiresArgument() {
		ArtemisNpc npc = new ArtemisNpc(1);
		npc.setVessel(ctx.getVesselData().getVessel(1500));
		new CommsOutgoingPacket(new ArtemisPlayer(0), npc, OtherMessage.GO_DEFEND, ctx);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDoesNotAcceptArgument() {
		new CommsOutgoingPacket(new ArtemisPlayer(0), new ArtemisBase(1), BaseMessage.BUILD_EMPS, 2, ctx);
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