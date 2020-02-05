package com.walkertribe.ian.world;

import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;

public class ArtemisGenericObjectTest {
	@Test(expected = IllegalArgumentException.class)
	public void testSetTypeNull() {
		new ArtemisGenericObject(47).setType(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetTypeIncompatible() {
		new ArtemisGenericObject(47).setType(ObjectType.ANOMALY);
	}

	@Test
	public void testUpdateFrom() {
		ArtemisGenericObject obj0 = new ArtemisGenericObject(47);
		ArtemisObjectTest.assertUnknownObject(obj0, 47, null);
		ArtemisGenericObject obj1 = new ArtemisGenericObject(47);
		obj1.setType(ObjectType.ASTEROID);
		obj1.setName("A");
		obj1.setX(1.0f);
		obj1.setY(2.0f);
		obj1.setZ(3.0f);
		ArtemisObjectTest.assertObject(obj1, 47, ObjectType.ASTEROID, "A", 1.0f, 2.0f, 3.0f);
		obj1.updateFrom(obj0);
		ArtemisObjectTest.assertObject(obj1, 47, ObjectType.ASTEROID, "A", 1.0f, 2.0f, 3.0f);
		obj0.updateFrom(obj1);
		ArtemisObjectTest.assertObject(obj0, 47, ObjectType.ASTEROID, "A", 1.0f, 2.0f, 3.0f);
		obj0.updateFrom(new ArtemisAnomaly(48));
	}
}
