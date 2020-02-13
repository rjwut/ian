package com.walkertribe.ian.enums;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.TestContext;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.TestUtil;
import com.walkertribe.ian.util.Util;
import com.walkertribe.ian.vesseldata.Faction;
import com.walkertribe.ian.vesseldata.MutableFaction;
import com.walkertribe.ian.vesseldata.MutableVessel;
import com.walkertribe.ian.vesseldata.MutableVesselData;
import com.walkertribe.ian.vesseldata.Vessel;
import com.walkertribe.ian.vesseldata.VesselAttribute;
import com.walkertribe.ian.world.ArtemisBase;
import com.walkertribe.ian.world.ArtemisCreature;
import com.walkertribe.ian.world.ArtemisNpc;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * Exercises all enums in the enums package.
 * @author rjwut
 */
public class EnumsTest {
	private static final List<Class<? extends Enum<?>>> ENUMS = new LinkedList<Class<? extends Enum<?>>>();
	private static final Set<CommsMessage> MESSAGE_HAS_ARGUMENT = new HashSet<CommsMessage>();
	private static final Set<ObjectType> NAMED_OBJECT_TYPES = new HashSet<ObjectType>();
	private static final Set<ObjectType> OBJECT_TYPES_WITH_ONE_MODEL = new HashSet<ObjectType>();
	private static final Map<Upgrade, Console> UPGRADE_ACTIVATION_CONSOLES = new HashMap<Upgrade, Console>();

	static {
		ENUMS.add(AlertStatus.class);
		ENUMS.add(AudioCommand.class);
		ENUMS.add(AudioMode.class);
		ENUMS.add(BaseMessage.class);
		ENUMS.add(BeamFrequency.class);
		ENUMS.add(CommsRecipientType.class);
		ENUMS.add(Origin.class);
		ENUMS.add(Console.class);
		ENUMS.add(ConsoleStatus.class);
		ENUMS.add(CreatureType.class);
		ENUMS.add(DriveType.class);
		ENUMS.add(EnemyMessage.class);
		ENUMS.add(FactionAttribute.class);
		ENUMS.add(GameType.class);
		ENUMS.add(MainScreenView.class);
		ENUMS.add(ObjectType.class);
		ENUMS.add(OrdnanceType.class);
		ENUMS.add(OtherMessage.class);
		ENUMS.add(Perspective.class);
		ENUMS.add(PlayerMessage.class);
		ENUMS.add(ShipSystem.class);
		ENUMS.add(SpecialAbility.class);
		ENUMS.add(TargetingMode.class);
		ENUMS.add(TubeState.class);
		ENUMS.add(Upgrade.class);

		MESSAGE_HAS_ARGUMENT.add(OtherMessage.GO_DEFEND);

		NAMED_OBJECT_TYPES.add(ObjectType.ANOMALY);
		NAMED_OBJECT_TYPES.add(ObjectType.BASE);
		NAMED_OBJECT_TYPES.add(ObjectType.CREATURE);
		NAMED_OBJECT_TYPES.add(ObjectType.GENERIC_MESH);
		NAMED_OBJECT_TYPES.add(ObjectType.NPC_SHIP);
		NAMED_OBJECT_TYPES.add(ObjectType.PLAYER_SHIP);

		OBJECT_TYPES_WITH_ONE_MODEL.add(ObjectType.ANOMALY);
		OBJECT_TYPES_WITH_ONE_MODEL.add(ObjectType.ASTEROID);
		OBJECT_TYPES_WITH_ONE_MODEL.add(ObjectType.DRONE);
		OBJECT_TYPES_WITH_ONE_MODEL.add(ObjectType.MINE);

		UPGRADE_ACTIVATION_CONSOLES.put(Upgrade.HIDENS_POWER_CELL, null);
		UPGRADE_ACTIVATION_CONSOLES.put(Upgrade.VIGORANIUM_NODULE, null);
		UPGRADE_ACTIVATION_CONSOLES.put(Upgrade.CETROCITE_HEATSINKS, Console.ENGINEERING);
		UPGRADE_ACTIVATION_CONSOLES.put(Upgrade.LATERAL_ARRAY, Console.SCIENCE);
		UPGRADE_ACTIVATION_CONSOLES.put(Upgrade.TAURON_FOCUSERS, Console.WEAPONS);
		UPGRADE_ACTIVATION_CONSOLES.put(Upgrade.INFUSION_P_COILS, Console.HELM);
		UPGRADE_ACTIVATION_CONSOLES.put(Upgrade.CARPACTION_COILS, Console.WEAPONS);
		UPGRADE_ACTIVATION_CONSOLES.put(Upgrade.SECRET_CODE_CASE, Console.COMMUNICATIONS);
	}

	@Test
	public void testAnomalyType() {
	    for (AnomalyType type : AnomalyType.values()) {
	        Upgrade upgrade = type.getUpgrade();

	        if (type == AnomalyType.BEACON || type == AnomalyType.SPACE_JUNK) {
	            Assert.assertNull(upgrade);
	        } else {
	            Assert.assertNotNull(upgrade);
	        }
	    }
	}

	@Test
	public void testBaseMessage() {
		for (BaseMessage value : BaseMessage.values()) {
			Assert.assertEquals(CommsRecipientType.BASE, value.getRecipientType());
			Assert.assertEquals(MESSAGE_HAS_ARGUMENT.contains(value), value.hasArgument());
		}

		for (OrdnanceType ordnance : OrdnanceType.values()) {
			Assert.assertEquals(ordnance, BaseMessage.build(ordnance).getOrdnanceType());
		}

		Assert.assertNull(BaseMessage.STAND_BY_FOR_DOCKING_OR_CEASE_OPERATION.getOrdnanceType());
		Assert.assertNull(BaseMessage.PLEASE_REPORT_STATUS.getOrdnanceType());
	}

	@Test
	public void testCommFilter() {
	    Set<CommFilter> filters = CommFilter.fromInt(7);
        Assert.assertTrue(filters.contains(CommFilter.ALERT));
        Assert.assertTrue(filters.contains(CommFilter.SIDE));
        Assert.assertTrue(filters.contains(CommFilter.STATUS));
        Assert.assertFalse(filters.contains(CommFilter.PLAYER));
        Assert.assertFalse(filters.contains(CommFilter.BASE));
        Assert.assertFalse(filters.contains(CommFilter.ENEMY));
        Assert.assertFalse(filters.contains(CommFilter.FRIEND));
        Assert.assertEquals(7, CommFilter.toInt(filters));
	}

	@Test
	public void testCommsRecipientType() {
		Assert.assertEquals(
				CommsRecipientType.BASE,
				CommsRecipientType.fromObject(new ArtemisBase(0), null)
		);
		ArtemisNpc npc = new ArtemisNpc(0);
		npc.setEnemy(BoolState.TRUE);
		Assert.assertEquals(
				CommsRecipientType.ENEMY,
				CommsRecipientType.fromObject(npc, null)
		);
		npc = new ArtemisNpc(0);
		npc.setEnemy(BoolState.FALSE);
		Assert.assertEquals(
				CommsRecipientType.OTHER,
				CommsRecipientType.fromObject(npc, null)
		);
		Assert.assertEquals(
				CommsRecipientType.PLAYER,
				CommsRecipientType.fromObject(new ArtemisPlayer(0), null)
		);
		Assert.assertNull(
				CommsRecipientType.fromObject(new ArtemisCreature(0), null)
		);
	}

	private Context buildContext() {
	    TestContext ctx = new TestContext();
        MutableVesselData vesselData = new MutableVesselData(ctx);
        ctx.setVesselData(vesselData);
        vesselData.putFaction(new MutableFaction(0, "TSN", "PLAYER"));
        vesselData.putFaction(new MutableFaction(1, "Terran", "FRIENDLY"));
        vesselData.putFaction(new MutableFaction(2, "Kralien", "ENEMY STANDARD"));
        vesselData.putVessel(new MutableVessel(ctx, 0, 0, "Light Cruiser", "PLAYER"));
        vesselData.putVessel(new MutableVessel(ctx, 1, 1, "Transport", "SMALL TRANSPORT"));
        vesselData.putVessel(new MutableVessel(ctx, 2, 2, "Cruiser", "SMALL"));
        return ctx;
	}

	@Test
	public void testCommsRecipientTypeRequiringContext() {
	    Context ctx = buildContext();
		ArtemisNpc npc = new ArtemisNpc(0);
		npc.setVessel(findNpcVessel(ctx, false));
		Assert.assertEquals(
				CommsRecipientType.ENEMY,
				CommsRecipientType.fromObject(npc, ctx)
		);
		npc = new ArtemisNpc(0);
		npc.setVessel(findNpcVessel(ctx, true));
		Assert.assertEquals(
				CommsRecipientType.OTHER,
				CommsRecipientType.fromObject(npc, ctx)
		);
	}

	@Test
	public void testIntelType() {
	    ArtemisNpc npc = new ArtemisNpc(1);
	    IntelType.RACE.set(npc, "Arvonian");
	    Assert.assertEquals("Arvonian", npc.getRace());
	    Assert.assertEquals("Arvonian", IntelType.RACE.get(npc));
	    IntelType.CLASS.set(npc, "Carrier");
        Assert.assertEquals("Carrier", npc.getArtemisClass());
        Assert.assertEquals("Carrier", IntelType.CLASS.get(npc));
        IntelType.LEVEL_1_SCAN.set(npc, "Level 1 scan");
        Assert.assertEquals("Level 1 scan", npc.getIntelLevel1());
        Assert.assertEquals("Level 1 scan", IntelType.LEVEL_1_SCAN.get(npc));
        IntelType.LEVEL_2_SCAN.set(npc, "Level 2 scan");
        Assert.assertEquals("Level 2 scan", npc.getIntelLevel2());
        Assert.assertEquals("Level 2 scan", IntelType.LEVEL_2_SCAN.get(npc));
	}

	@Test
	public void testOrdnanceType() {
	    OrdnanceType type = OrdnanceType.fromCode("bea");
        Assert.assertEquals(OrdnanceType.BEACON, type);
        Assert.assertEquals("bea", type.getCode());
        Assert.assertNull(OrdnanceType.fromCode("zzz"));
	}

	@Test
	public void testOrigin() {
		Assert.assertEquals(Origin.SERVER, Origin.fromInt(1));
		Assert.assertEquals(Origin.CLIENT, Origin.fromInt(2));
		Assert.assertNull(Origin.fromInt(0));
		Assert.assertEquals(Origin.CLIENT, Origin.SERVER.opposite());
		Assert.assertEquals(Origin.SERVER, Origin.CLIENT.opposite());
	}

	@Test
	public void testConsoleToString() {
		Assert.assertEquals("Captain's map", Console.CAPTAINS_MAP.toString());
	}

	@Test
	public void testEnemyMessage() {
		for (EnemyMessage value : EnemyMessage.values()) {
			Assert.assertEquals(CommsRecipientType.ENEMY, value.getRecipientType());
			Assert.assertEquals(MESSAGE_HAS_ARGUMENT.contains(value), value.hasArgument());
		}
	}

	@Test
	public void testFactionAttribute() {
		Set<FactionAttribute> attrs = FactionAttribute.build("player");
		Assert.assertEquals("PLAYER", Util.enumSetToString(attrs));
		attrs = FactionAttribute.build("enemy loner biomech");
		Assert.assertEquals("ENEMY LONER BIOMECH", Util.enumSetToString(attrs));

		// conditionals coverage for some valid combinations
		FactionAttribute.build("enemy standard whalelover");
		FactionAttribute.build("enemy standard whalehater");
		FactionAttribute.build("player jumpmaster");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFactionAttributeMustHaveStance() {
		FactionAttribute.build("standard");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFactionAttributeEnemyMustHaveOrg() {
		FactionAttribute.build("enemy");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFactionAttributeWhaleAttitudeExclusivity() {
		FactionAttribute.build("enemy standard whalelover whalehater");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFactionAttributeNonPlayerJumpMaster() {
		FactionAttribute.build("enemy standard jumpmaster");
	}

	@Test
	public void testObjectType() throws ReflectiveOperationException {
		for (ObjectType type : ObjectType.values()) {
			Assert.assertEquals(type, ObjectType.fromId(type.getId()));
			Assert.assertEquals(NAMED_OBJECT_TYPES.contains(type), type.isNamed());
			Class<? extends ArtemisObject> clazz = type.getObjectClass();
			Constructor<? extends ArtemisObject> constructor = clazz.getConstructor(int.class);
			Assert.assertTrue(type.isCompatible(constructor.newInstance(0)));
			float scale = OBJECT_TYPES_WITH_ONE_MODEL.contains(type) ? ObjectType.MODEL_SCALE : 0;
			Assert.assertEquals(scale, type.getScale(), TestUtil.EPSILON);
		}

		Assert.assertNull(ObjectType.fromId(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testObjectTypeInvalidId() {
		ObjectType.fromId(9);
	}

	@Test
	public void testOrdnanceTypeToString() {
		Assert.assertEquals("Homing", OrdnanceType.HOMING.toString());
		Assert.assertEquals("Nuke", OrdnanceType.NUKE.toString());
		Assert.assertEquals("Mine", OrdnanceType.MINE.toString());
		Assert.assertEquals("EMP", OrdnanceType.EMP.toString());
		Assert.assertEquals("PlasmaShock", OrdnanceType.PSHOCK.toString());
	}

	@Test
	public void testOtherMessage() {
		for (OtherMessage value : OtherMessage.values()) {
			Assert.assertEquals(value, OtherMessage.fromId(value.getId()));
			Assert.assertEquals(CommsRecipientType.OTHER, value.getRecipientType());
			Assert.assertEquals(MESSAGE_HAS_ARGUMENT.contains(value), value.hasArgument());
		}

		Assert.assertNull(OtherMessage.fromId(-1));
	}

	@Test
	public void testPlayerMessage() {
		for (PlayerMessage value : PlayerMessage.values()) {
			Assert.assertEquals(CommsRecipientType.PLAYER, value.getRecipientType());
			Assert.assertEquals(MESSAGE_HAS_ARGUMENT.contains(value), value.hasArgument());
		}
	}

	@Test
	public void testSpecialAbility() {
		Set<SpecialAbility> set = SpecialAbility.fromValue(0);
		Assert.assertTrue(set.isEmpty());
		set = SpecialAbility.fromValue(0x1555);

		for (SpecialAbility ability : SpecialAbility.values()) {
			boolean on = ability.ordinal() % 2 == 0;
			Assert.assertEquals(on, set.contains(ability));
			Assert.assertEquals(on, ability.on(0x1555));
		}
	}

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeInvalidIndex1() {
        Upgrade.fromIndex(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeInvalidIndex2() {
        Upgrade.fromIndex(Upgrade.values().length - Upgrade.INFUSION_P_COILS.ordinal());
    }

	@Test
	public void testUpgradeActivations() {
		for (Map.Entry<Upgrade, Console> entry : UPGRADE_ACTIVATION_CONSOLES.entrySet()) {
			Assert.assertEquals(entry.getKey().getActivatedBy(), entry.getValue());
		}
	}

	@Test
	public void coverValueOf() {
		for (Class<? extends Enum<?>> clazz : ENUMS) {
			TestUtil.coverEnumValueOf(clazz);
		}
	}

	private static Vessel findNpcVessel(Context ctx, boolean friendly) {
		for (Iterator<Vessel> iter = ctx.getVesselData().vesselIterator(); iter.hasNext(); ) {
			Vessel vessel = iter.next();

			if (vessel.is(VesselAttribute.BASE) || vessel.is(VesselAttribute.PLAYER)) {
				continue;
			}

			Faction faction = vessel.getFaction();

			if (faction.is(FactionAttribute.PLAYER)) {
				continue;
			}

			if (friendly ^ faction.is(FactionAttribute.ENEMY)) {
				return vessel;
			}
		}

		return null; // shouldn't happen
	}
}
