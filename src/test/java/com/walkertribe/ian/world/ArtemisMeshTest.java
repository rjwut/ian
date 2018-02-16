package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisMeshTest {
	@Test
	public void testUpdateFrom() {
		ArtemisMesh obj0 = new ArtemisMesh(47);
		assertMesh(obj0, 47, null, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, null, null, -1, -1, -1,
				Float.MIN_VALUE, Float.MIN_VALUE);
		ArtemisMesh obj1 = new ArtemisMesh(47);
		obj1.setName("MESH");
		obj1.setX(1f);
		obj1.setY(2f);
		obj1.setZ(3f);
		obj1.setMesh("test.dxs");
		obj1.setTexture("test.png");
		obj1.setRed(0.25f);
		obj1.setGreen(0.5f);
		obj1.setBlue(0.75f);
		obj1.setFakeShields(100, 75);
		assertMesh(obj1, 47, "MESH", 1f, 2f, 3f, "test.dxs", "test.png", 0.25f, 0.5f, 0.75f, 100, 75);
		obj1.updateFrom(obj0, null);
		assertMesh(obj1, 47, "MESH", 1f, 2f, 3f, "test.dxs", "test.png", 0.25f, 0.5f, 0.75f, 100, 75);
		obj0.updateFrom(obj1, null);
		assertMesh(obj0, 47, "MESH", 1f, 2f, 3f, "test.dxs", "test.png", 0.25f, 0.5f, 0.75f, 100, 75);
		obj0.updateFrom(new ArtemisAnomaly(48), null);
	}

	public static void assertMesh(ArtemisMesh obj, int id, String name, float x, float y, float z, String mesh,
			String texture, float red, float green, float blue, float shieldsFront, float shieldsRear) {
		ArtemisObjectTest.assertObject(obj, id, ObjectType.GENERIC_MESH, name, x, y, z);
		TestUtil.assertToStringEquals(mesh, obj.getMesh());
		TestUtil.assertToStringEquals(texture, obj.getTexture());
		Assert.assertEquals(red, obj.getRed(), TestUtil.EPSILON);
		Assert.assertEquals(green, obj.getGreen(), TestUtil.EPSILON);
		Assert.assertEquals(blue, obj.getBlue(), TestUtil.EPSILON);
		Assert.assertEquals(shieldsFront, obj.getShieldsFront(), TestUtil.EPSILON);
		Assert.assertEquals(shieldsRear, obj.getShieldsRear(), TestUtil.EPSILON);
	}
}
