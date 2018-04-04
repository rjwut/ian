package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisCreatureTest {
	@Test
	public void testUpdateFrom() {
		ArtemisCreature obj0 = new ArtemisCreature(47);
		assertUnknownCreature(obj0, 47);
		ArtemisCreature obj1 = new ArtemisCreature(47);
		obj1.setX(1.0f);
		obj1.setY(2.0f);
		obj1.setZ(3.0f);
		obj1.setName("WHALE");
		obj1.setHeading(4.0f);
		obj1.setPitch(5.0f);
		obj1.setRoll(6.0f);
		obj1.setCreatureType(CreatureType.WHALE);
		obj1.setHealth(50);
		obj1.setMaxHealth(100);
		assertCreature(obj1, 47, 1.0f, 2.0f, 3.0f, "WHALE", 4.0f, 5.0f, 6.0f, CreatureType.WHALE, 50, 100);
		obj1.updateFrom(obj0);
		assertCreature(obj1, 47, 1.0f, 2.0f, 3.0f, "WHALE", 4.0f, 5.0f, 6.0f, CreatureType.WHALE, 50, 100);
		obj0.updateFrom(obj1);
		assertCreature(obj0, 47, 1.0f, 2.0f, 3.0f, "WHALE", 4.0f, 5.0f, 6.0f, CreatureType.WHALE, 50, 100);
		obj0.updateFrom(new ArtemisDrone(47));
	}

	public static void assertUnknownCreature(ArtemisCreature creature, int id) {
		ArtemisObjectTest.assertUnknownObject(creature, id, ObjectType.CREATURE);
		ArtemisOrientableTest.assertUnknownOrientable(creature);
		Assert.assertNull(creature.getCreatureType());
		Assert.assertTrue(Float.isNaN(creature.getHealth()));
		Assert.assertTrue(Float.isNaN(creature.getMaxHealth()));
	}

	public static void assertCreature(ArtemisCreature creature, int id, float x, float y, float z, String name,
			float heading, float pitch, float roll, CreatureType type, float health, float maxHealth) {
		ArtemisObjectTest.assertObject(creature, id, ObjectType.CREATURE, name, x, y, z);
		ArtemisOrientableTest.assertOrientable(creature, heading, pitch, roll);
		Assert.assertEquals(type, creature.getCreatureType());
		Assert.assertEquals(health, creature.getHealth(), TestUtil.EPSILON);
		Assert.assertEquals(maxHealth, creature.getMaxHealth(), TestUtil.EPSILON);
	}
}
