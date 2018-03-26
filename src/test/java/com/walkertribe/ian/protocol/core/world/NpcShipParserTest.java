package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.SpecialAbility;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.world.ArtemisNpc;
import com.walkertribe.ian.world.ArtemisNpcTest;

public class NpcShipParserTest extends AbstractObjectUpdatePacketTester<ArtemisNpc> {
	@Test
	public void test() {
		execute(NpcShipParser.class, 3);
	}

	@Override
	protected void testObjects(List<ArtemisNpc> objects) {
		Context ctx = getContext();
		ArtemisNpcTest.assertAllProps(objects.get(0));
		ArtemisNpc npc = objects.get(1);
		Assert.assertEquals(0x05, npc.getSpecialBits());
		Assert.assertEquals(0x01, npc.getSpecialStateBits());
		Assert.assertNull(npc.getSpecialAbilities(ctx));
		Assert.assertEquals(BoolState.UNKNOWN, npc.hasSpecialAbility(SpecialAbility.STEALTH, ctx));
		Assert.assertEquals(BoolState.UNKNOWN, npc.isUsingSpecialAbility(SpecialAbility.STEALTH, ctx));
		npc = objects.get(2);
		Assert.assertEquals(0x05, npc.getSpecialBits());
		Assert.assertEquals(0x01, npc.getSpecialStateBits());
		Assert.assertTrue(npc.getSpecialAbilities(ctx).isEmpty());
		Assert.assertEquals(BoolState.FALSE, npc.hasSpecialAbility(SpecialAbility.STEALTH, ctx));
		Assert.assertEquals(BoolState.FALSE, npc.isUsingSpecialAbility(SpecialAbility.STEALTH, ctx));
	}

	protected Context getContext() {
		return ArtemisNpcTest.buildContext();
	}
}
