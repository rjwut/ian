package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisMeshTest {
	@Test
	public void testUpdateFrom() {
		ArtemisMesh obj0 = new ArtemisMesh(47);
		assertUnknownMesh(obj0, 47);
		ArtemisMesh obj1 = new ArtemisMesh(47);
		obj1.setName("MESH");
		obj1.setX(1f);
		obj1.setY(2f);
		obj1.setZ(3f);
		obj1.setRoll(4f);
		obj1.setPitch(5f);
		obj1.setHeading(6f);
		obj1.setRollDelta(7f);
		obj1.setPitchDelta(8f);
		obj1.setHeadingDelta(9f);
		obj1.setMesh("test.dxs");
		obj1.setTexture("test.png");
		obj1.setPushRadius(10f);
		obj1.setBlockFire(BoolState.TRUE);
		obj1.setScale(11f);
		obj1.setRed(0.25f);
		obj1.setGreen(0.5f);
		obj1.setBlue(0.75f);
		obj1.setFakeShields(100, 75);
		assertMesh(obj1, 47, "MESH", 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, "test.dxs", "test.png", 10f, BoolState.TRUE,
				11f, 0.25f, 0.5f, 0.75f, 100, 75);
		obj1.updateFrom(obj0);
		assertMesh(obj1, 47, "MESH", 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, "test.dxs", "test.png", 10f, BoolState.TRUE,
				11f, 0.25f, 0.5f, 0.75f, 100, 75);
		obj0.updateFrom(obj1);
		assertMesh(obj0, 47, "MESH", 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, "test.dxs", "test.png", 10f, BoolState.TRUE,
				11f, 0.25f, 0.5f, 0.75f, 100, 75);
		obj0.updateFrom(new ArtemisAnomaly(48));
	}

	public static void assertUnknownMesh(ArtemisMesh obj, int id) {
		ArtemisObjectTest.assertUnknownObject(obj, id, ObjectType.GENERIC_MESH);
		Assert.assertNull(obj.getMesh());
		Assert.assertNull(obj.getTexture());
		Assert.assertTrue(Float.isNaN(obj.getRed()));
		Assert.assertTrue(Float.isNaN(obj.getGreen()));
		Assert.assertTrue(Float.isNaN(obj.getBlue()));
		Assert.assertTrue(Float.isNaN(obj.getShieldsFront()));
		Assert.assertTrue(Float.isNaN(obj.getShieldsRear()));
	}

	public static void assertMesh(ArtemisMesh obj, int id, String name, float x, float y, float z, float roll,
			float pitch, float heading, float rollDelta, float pitchDelta, float headingDelta, String mesh,
			String texture, float pushRadius, BoolState blockFire, float scale, float red, float green, float blue,
			float shieldsFront, float shieldsRear) {
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
