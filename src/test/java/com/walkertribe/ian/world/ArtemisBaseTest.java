package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;

public class ArtemisBaseTest {
	@Test
	public void testUpdateFrom() {
		ArtemisBase obj0 = new ArtemisBase(47);
		assertBase(obj0, 47, null, Float.MIN_VALUE, Float.MIN_VALUE, -1, -1, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
		ArtemisBase obj1 = new ArtemisBase(47);
		obj1.setName("DS1");
		obj1.setShieldsFront(47.0f);
		obj1.setShieldsRear(0.0f);
		obj1.setIndex(0);
		obj1.setHullId(1000);
		obj1.setX(1.0f);
		obj1.setY(2.0f);
		obj1.setZ(3.0f);
		assertBase(obj1, 47, "DS1", 47.0f, 0.0f, 0, 1000, 1.0f, 2.0f, 3.0f);
		obj1.updateFrom(obj0, null);
		assertBase(obj1, 47, "DS1", 47.0f, 0.0f, 0, 1000, 1.0f, 2.0f, 3.0f);
		obj0.updateFrom(obj1, null);
		assertBase(obj0, 47, "DS1", 47.0f, 0.0f, 0, 1000, 1.0f, 2.0f, 3.0f);
		obj0.updateFrom(new ArtemisAnomaly(48), null);
	}

	public static void assertBase(ArtemisBase base, int id, String name, float shieldsFront, float shieldsRear,
			int index, int hullId, float x, float y, float z) {
		ArtemisShieldedTest.assertShielded(base, id, ObjectType.BASE, name, x, y, z, hullId, shieldsFront, shieldsRear);
		Assert.assertEquals(index, base.getIndex());
	}
}
