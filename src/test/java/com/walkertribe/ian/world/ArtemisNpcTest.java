package com.walkertribe.ian.world;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.TestContext;
import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.enums.SpecialAbility;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.TestUtil;
import com.walkertribe.ian.util.Util;
import com.walkertribe.ian.vesseldata.MutableFaction;
import com.walkertribe.ian.vesseldata.MutableVessel;
import com.walkertribe.ian.vesseldata.MutableVesselData;

public class ArtemisNpcTest {
	private static final float[] SHIELD_FREQS_UNSPECIFIED;
	private static final float[] SHIELD_FREQS;
	private static final float[] SYS_DAMAGE_UNSPECIFIED;
	private static final float[] SYS_DAMAGE;

	private static final Context CTX;

	static {
		int freqCount = BeamFrequency.values().length;
		float delta = 1f / freqCount;
		SHIELD_FREQS_UNSPECIFIED = new float[freqCount];
		SHIELD_FREQS = new float[freqCount];

		for (int i = 0; i < freqCount; i++) {
			SHIELD_FREQS_UNSPECIFIED[i] = -1f;
			SHIELD_FREQS[i] = delta * i;
		}

		SYS_DAMAGE_UNSPECIFIED = new float[Artemis.SYSTEM_COUNT];
		SYS_DAMAGE = new float[Artemis.SYSTEM_COUNT];
		delta = 1f / Artemis.SYSTEM_COUNT;

		for (int i = 0; i < Artemis.SYSTEM_COUNT; i++) {
			SYS_DAMAGE_UNSPECIFIED[i] = -1f;
			SYS_DAMAGE[i] = delta * i;
		}

		CTX = buildContext();
	}

	@Test
	public void testUpdateFrom() {
		ArtemisNpc obj0 = new ArtemisNpc(47);
		assertUnknownNpc(obj0, 47);
		ArtemisNpc obj1 = new ArtemisNpc(47);
		obj1.setName("TEST");
		obj1.setX(1f);
		obj1.setY(2f);
		obj1.setZ(3f);
		obj1.setHullId(2000);
		obj1.setShieldsFront(100f);
		obj1.setShieldsRear(47f);
		obj1.setHeading(0.3f);
		obj1.setPitch(0.2f);
		obj1.setRoll(0.1f);
		obj1.setVelocity(0.4f);
		obj1.setShieldsFrontMax(100);
		obj1.setShieldsRearMax(100);

		for (BeamFrequency freq : BeamFrequency.values()) {
			obj1.setShieldFreq(freq, SHIELD_FREQS[freq.ordinal()]);
		}

		obj1.setSteering(0.2f);
		obj1.setTopSpeed(0.8f);
		obj1.setTurnRate(0.15f);
		obj1.setImpulse(0.7f);
		obj1.setScanLevelBits(1, 2);
		obj1.setScanLevelBits(2, 2);
		obj1.setSpecialBits(0x05);
		obj1.setSpecialStateBits(0x01);
		obj1.setEnemy(BoolState.TRUE);
		obj1.setSurrendered(BoolState.TRUE);
		obj1.setFleetNumber((byte) 2);

		for (ShipSystem sys : ShipSystem.values()) {
			obj1.setSystemDamage(sys, SYS_DAMAGE[sys.ordinal()]);
		}

		assertAllProps(obj1);
		obj1.updateFrom(obj0);
		assertAllProps(obj1);
		obj0.updateFrom(obj1);
		assertAllProps(obj0);
		obj0.updateFrom(new ArtemisAnomaly(48));
	}

	@Test
	public void testUnknownVesselSpecials() {
		ArtemisNpc obj = new ArtemisNpc(47);
		obj.setSpecialBits(0x05);
		obj.setSpecialStateBits(0x01);
		Assert.assertEquals(0x05, obj.getSpecialBits());
		Assert.assertEquals(0x01, obj.getSpecialStateBits());
		Assert.assertNull(obj.getSpecialAbilities(CTX));
		Assert.assertEquals(BoolState.UNKNOWN, obj.hasSpecialAbility(SpecialAbility.STEALTH, CTX));
		Assert.assertEquals(BoolState.UNKNOWN, obj.isUsingSpecialAbility(SpecialAbility.STEALTH, CTX));
	}

	@Test
	public void testNonSpecialVesselWithSpecialData() {
		ArtemisNpc obj = new ArtemisNpc(47);
		obj.setHullId(1000);
		obj.setSpecialBits(0x05);
		obj.setSpecialStateBits(0x01);
		Assert.assertEquals(0x05, obj.getSpecialBits());
		Assert.assertEquals(0x01, obj.getSpecialStateBits());
		Assert.assertTrue(obj.getSpecialAbilities(CTX).isEmpty());
		Assert.assertEquals(BoolState.FALSE, obj.hasSpecialAbility(SpecialAbility.STEALTH, CTX));
		Assert.assertEquals(BoolState.FALSE, obj.isUsingSpecialAbility(SpecialAbility.STEALTH, CTX));
	}

	@Test
	public void testAppendObjectPropsSpecials() {
		ArtemisNpc obj = new ArtemisNpc(47);
		SortedMap<String, Object> props = new TreeMap<String, Object>();
		obj.appendObjectProps(props);
		Assert.assertFalse(props.containsKey("Specials"));
		Assert.assertFalse(props.containsKey("Specials active"));
		props.clear();
		obj.appendObjectProps(props);
		Assert.assertNull(props.get("Specials"));
		Assert.assertNull(props.get("Specials active"));
		obj.setSpecialBits(0x00);
		obj.setSpecialStateBits(0x00);
		props.clear();
		obj.appendObjectProps(props);
		Assert.assertEquals("NONE", props.get("Specials"));
		Assert.assertEquals("NONE", props.get("Specials active"));
		obj.setSpecialBits(0x05);
		obj.setSpecialStateBits(0x01);
		props.clear();
		obj.appendObjectProps(props);
		Assert.assertEquals("STEALTH CLOAK", props.get("Specials"));
		Assert.assertEquals("STEALTH", props.get("Specials active"));
		
	}

	public static void assertAllProps(ArtemisNpc npc) {
		assertNpc(npc, 47, ObjectType.NPC_SHIP, "TEST", 1f, 2f, 3f, 2000, 100f, 47f, 0.3f, 0.2f, 0.1f, 0.4f, 100, 100,
				SHIELD_FREQS, 0.2f, 0.8f, 0.15f, 0.7f, 2, 2, 0x05, 0x01, BoolState.TRUE, BoolState.TRUE, (byte) 2,
				SYS_DAMAGE);
	}

	private static void assertUnknownNpc(ArtemisNpc npc, int id) {
		ArtemisShieldedTest.assertUnknownShielded(npc, id, ObjectType.NPC_SHIP);
		ArtemisOrientableTest.assertUnknownOrientable(npc);
		BaseArtemisShipTest.assertUnknownShip(npc);
	}

	private static void assertNpc(ArtemisNpc npc, int id, ObjectType type, String name, float x, float y, float z,
			int hullId, float shieldsFront, float shieldsRear, float heading, float pitch, float roll, float velocity,
			float shieldsFrontMax, float shieldsRearMax, float[] shieldFreqs, float steering, float topSpeed,
			float turnRate, float impulse, Integer scanLevel1, Integer scanLevel2, int special, int specialState,
			BoolState enemy, BoolState surrendered, byte fleetNumber, float[] sysDamage) {
		ArtemisShieldedTest.assertShielded(npc, id, type, name, x, y, z, hullId, shieldsFront, shieldsRear);
		ArtemisOrientableTest.assertOrientable(npc, heading, pitch, roll);
		BaseArtemisShipTest.assertShip(npc, velocity, shieldsFrontMax, shieldsRearMax, shieldFreqs, steering, topSpeed,
				turnRate, impulse);
		Assert.assertEquals(scanLevel1, npc.getScanLevelBits(1));
		Assert.assertEquals(scanLevel2, npc.getScanLevelBits(2));
		Assert.assertEquals(special, npc.getSpecialBits());
		Assert.assertEquals(specialState, npc.getSpecialStateBits());

		if (special != -1) {
			Set<SpecialAbility> expectedSpecials = SpecialAbility.fromValue(special);
			Assert.assertEquals(
					Util.enumSetToString(expectedSpecials),
					Util.enumSetToString(npc.getSpecialAbilities(CTX))
			);

			for (SpecialAbility curSpecial : SpecialAbility.values()) {
				Assert.assertEquals(
						expectedSpecials.contains(curSpecial),
						npc.hasSpecialAbility(curSpecial, CTX).getBooleanValue()
				);
			}

			expectedSpecials = SpecialAbility.fromValue(specialState);

			for (SpecialAbility curSpecial : SpecialAbility.values()) {
				Assert.assertEquals(
						expectedSpecials.contains(curSpecial),
						npc.isUsingSpecialAbility(curSpecial, CTX).getBooleanValue()
				);
			}
		} else {
			Assert.assertNull(npc.getSpecialAbilities(CTX));

			for (SpecialAbility curSpecial : SpecialAbility.values()) {
				Assert.assertEquals(BoolState.UNKNOWN, npc.hasSpecialAbility(curSpecial, CTX));
				Assert.assertEquals(BoolState.UNKNOWN, npc.isUsingSpecialAbility(curSpecial, CTX));
			}
		}

		Assert.assertEquals(enemy, npc.isEnemy());
		Assert.assertEquals(surrendered, npc.isSurrendered());
		Assert.assertEquals(fleetNumber, npc.getFleetNumber());

		for (ShipSystem sys : ShipSystem.values()) {
			Assert.assertEquals(sysDamage[sys.ordinal()], npc.getSystemDamage(sys), TestUtil.EPSILON);
		}
	}

	public static Context buildContext() {
		TestContext ctx = new TestContext();
		MutableVesselData vesselData = new MutableVesselData(ctx);
		ctx.setVesselData(vesselData);
		vesselData.putFaction(new MutableFaction(0, "TSN", "player"));
		vesselData.putFaction(new MutableFaction(1, "Skaraan", "enemy loner hasspecials"));
		vesselData.putVessel(new MutableVessel(ctx, 1000, 0, "Cruiser", "small"));
		vesselData.putVessel(new MutableVessel(ctx, 2000, 1, "Defiler", "small"));
		return ctx;
	}
}
