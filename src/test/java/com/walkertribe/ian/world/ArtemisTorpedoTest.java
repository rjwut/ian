package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.OrdnanceType;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisTorpedoTest {
	@Test
	public void testUpdateFrom() {
		ArtemisTorpedo obj0 = new ArtemisTorpedo(47);
		assertUnpopulatedTorpedo(obj0, 47);
		ArtemisTorpedo obj1 = new ArtemisTorpedo(47);
		obj1.setX(1f);
		obj1.setY(2f);
		obj1.setZ(3f);
		obj1.setDx(4f);
		obj1.setDy(5f);
		obj1.setDz(6f);
		obj1.setOrdnanceType(OrdnanceType.EMP);
		obj1.updateFrom(obj0);
		assertPopulatedTorpedo(obj1);
		obj0.updateFrom(obj1);
		assertPopulatedTorpedo(obj0);
		obj0.updateFrom(new ArtemisCreature(48));
		assertPopulatedTorpedo(obj0);
	}

	public static void assertUnpopulatedTorpedo(ArtemisTorpedo torp, int id) {
		ArtemisObjectTest.assertUnknownObject(torp, id, ObjectType.TORPEDO);
		Assert.assertTrue(Float.isNaN(torp.getDx()));
		Assert.assertTrue(Float.isNaN(torp.getDy()));
		Assert.assertTrue(Float.isNaN(torp.getDz()));
		Assert.assertNull(torp.getOrdnanceType());
	}

	public static void assertPopulatedTorpedo(ArtemisTorpedo torp) {
		assertTorpedo(torp, 47, 1f, 2f, 3f, 4f, 5f, 6f, OrdnanceType.EMP);
	}

	public static void assertTorpedo(ArtemisTorpedo torp, int id, float x, float y, float z, float dx, float dy,
			float dz, OrdnanceType ordType) {
		ArtemisObjectTest.assertObject(torp, id, ObjectType.TORPEDO, null, x, y, z);
		Assert.assertEquals(dx, torp.getDx(), TestUtil.EPSILON);
		Assert.assertEquals(dy, torp.getDy(), TestUtil.EPSILON);
		Assert.assertEquals(dz, torp.getDz(), TestUtil.EPSILON);
		Assert.assertEquals(ordType, torp.getOrdnanceType());
	}
}
