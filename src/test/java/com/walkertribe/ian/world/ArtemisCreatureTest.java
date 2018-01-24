package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.ObjectType;

public class ArtemisCreatureTest {
	@Test
	public void testUpdateFrom() {
		ArtemisCreature obj0 = new ArtemisCreature(47);
		assertCreature(obj0, 47, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, null, Float.MIN_VALUE,
				Float.MIN_VALUE, Float.MIN_VALUE, null);
		ArtemisCreature obj1 = new ArtemisCreature(47);
		obj1.setX(1.0f);
		obj1.setY(2.0f);
		obj1.setZ(3.0f);
		obj1.setName("WHALE");
		obj1.setHeading(4.0f);
		obj1.setPitch(5.0f);
		obj1.setRoll(6.0f);
		obj1.setCreatureType(CreatureType.WHALE);
		assertCreature(obj1, 47, 1.0f, 2.0f, 3.0f, "WHALE", 4.0f, 5.0f, 6.0f, CreatureType.WHALE);
		obj1.updateFrom(obj0, null);
		assertCreature(obj1, 47, 1.0f, 2.0f, 3.0f, "WHALE", 4.0f, 5.0f, 6.0f, CreatureType.WHALE);
		obj0.updateFrom(obj1, null);
		assertCreature(obj0, 47, 1.0f, 2.0f, 3.0f, "WHALE", 4.0f, 5.0f, 6.0f, CreatureType.WHALE);
	}

	public static void assertCreature(ArtemisCreature creature, int id, float x, float y, float z, String name,
			float heading, float pitch, float roll, CreatureType type) {
		ArtemisObjectTest.assertObject(creature, id, ObjectType.CREATURE, name, x, y, z);
		ArtemisOrientableTest.assertOrientable(creature, heading, pitch, roll);
		Assert.assertEquals(type, creature.getCreatureType());
	}
}
