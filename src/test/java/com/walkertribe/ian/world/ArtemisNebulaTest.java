package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisNebulaTest {
	@Test
	public void testUpdateFrom() {
		ArtemisNebula obj0 = new ArtemisNebula(47);
		assertNebula(obj0, 47, null, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, -1, -1, -1);
		ArtemisNebula obj1 = new ArtemisNebula(47);
		obj1.setName("NEBULA");
		obj1.setX(1f);
		obj1.setY(2f);
		obj1.setZ(3f);
		obj1.setRed(0.25f);
		obj1.setGreen(0.5f);
		obj1.setBlue(0.75f);
		assertNebula(obj1, 47, "NEBULA", 1f, 2f, 3f, 0.25f, 0.5f, 0.75f);
		obj1.updateFrom(obj0, null);
		assertNebula(obj1, 47, "NEBULA", 1f, 2f, 3f, 0.25f, 0.5f, 0.75f);
		obj0.updateFrom(obj1, null);
		assertNebula(obj0, 47, "NEBULA", 1f, 2f, 3f, 0.25f, 0.5f, 0.75f);
		obj0.updateFrom(new ArtemisAnomaly(48), null);
	}

	public static void assertNebula(ArtemisNebula obj, int id, String name, float x, float y, float z, float red,
			float green, float blue) {
		ArtemisObjectTest.assertObject(obj, id, ObjectType.NEBULA, name, x, y, z);
		Assert.assertEquals(red, obj.getRed(), TestUtil.EPSILON);
		Assert.assertEquals(green, obj.getGreen(), TestUtil.EPSILON);
		Assert.assertEquals(blue, obj.getBlue(), TestUtil.EPSILON);
	}
}
