package com.walkertribe.ian.world;

import org.junit.Assert;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisShieldedTest {
	public static void assertUnknownShielded(ArtemisShielded obj, int id, ObjectType type) {
		ArtemisObjectTest.assertUnknownObject(obj, id, type);
		Assert.assertEquals(-1, obj.getHullId());
		Assert.assertTrue(Float.isNaN(obj.getShieldsFront()));
		Assert.assertTrue(Float.isNaN(obj.getShieldsRear()));
	}

	public static void assertShielded(ArtemisShielded obj, int id, ObjectType type, String name, float x, float y,
			float z, int hullId, float shieldsFront, float shieldsRear) {
		ArtemisObjectTest.assertObject(obj, id, type, name, x, y, z);
		Assert.assertEquals(hullId, obj.getHullId());
		Assert.assertEquals(shieldsFront, obj.getShieldsFront(), TestUtil.EPSILON);
		Assert.assertEquals(shieldsRear, obj.getShieldsRear(), TestUtil.EPSILON);
	}
}
