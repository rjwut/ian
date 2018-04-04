package com.walkertribe.ian.world;

import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;

public class ArtemisBaseTest {
	@Test
	public void testUpdateFrom() {
		ArtemisBase obj0 = new ArtemisBase(47);
		assertUnknownBase(obj0, 47);
		ArtemisBase obj1 = new ArtemisBase(47);
		obj1.setName("DS1");
		obj1.setShieldsFront(47.0f);
		obj1.setShieldsRear(0.0f);
		obj1.setHullId(1000);
		obj1.setX(1.0f);
		obj1.setY(2.0f);
		obj1.setZ(3.0f);
		assertBase(obj1, 47, "DS1", 47.0f, 0.0f, 1000, 1.0f, 2.0f, 3.0f);
		obj1.updateFrom(obj0);
		assertBase(obj1, 47, "DS1", 47.0f, 0.0f, 1000, 1.0f, 2.0f, 3.0f);
		obj0.updateFrom(obj1);
		assertBase(obj0, 47, "DS1", 47.0f, 0.0f, 1000, 1.0f, 2.0f, 3.0f);
		obj0.updateFrom(new ArtemisAnomaly(48));
	}

	public static void assertUnknownBase(ArtemisBase base, int id) {
		ArtemisShieldedTest.assertUnknownShielded(base, id, ObjectType.BASE);
	}

	public static void assertBase(ArtemisBase base, int id, String name, float shieldsFront, float shieldsRear,
			int hullId, float x, float y, float z) {
		ArtemisShieldedTest.assertShielded(base, id, ObjectType.BASE, name, x, y, z, hullId, shieldsFront, shieldsRear);
	}
}
