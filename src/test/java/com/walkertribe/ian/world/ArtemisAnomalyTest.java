package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.AnomalyType;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisAnomalyTest {
	@Test
	public void testUpdateFrom() {
		ArtemisAnomaly obj0 = new ArtemisAnomaly(47);
		assertUnknownAnomaly(obj0);
		ArtemisAnomaly obj1 = new ArtemisAnomaly(47);
		obj1.setX(1.0f);
		obj1.setY(2.0f);
		obj1.setZ(3.0f);
		obj1.setAnomalyType(AnomalyType.LATERAL_ARRAY);
		obj1.updateFrom(obj0);
		assertAnomaly(obj1, 1.0f, 2.0f, 3.0f, AnomalyType.LATERAL_ARRAY);
		obj0.updateFrom(obj1);
		assertAnomaly(obj0, 1.0f, 2.0f, 3.0f, AnomalyType.LATERAL_ARRAY);
		obj0.updateFrom(new ArtemisBase(48));
	}

	public static void assertUnknownAnomaly(ArtemisAnomaly obj) {
		Assert.assertTrue(Float.isNaN(obj.getX()));
		Assert.assertTrue(Float.isNaN(obj.getY()));
		Assert.assertTrue(Float.isNaN(obj.getZ()));
		Assert.assertNull(obj.getAnomalyType());
	}

	public static void assertAnomaly(ArtemisAnomaly obj, float x, float y, float z, AnomalyType anomalyType) {
		Assert.assertEquals(x, obj.getX(), TestUtil.EPSILON);
		Assert.assertEquals(y, obj.getY(), TestUtil.EPSILON);
		Assert.assertEquals(z, obj.getZ(), TestUtil.EPSILON);
		Assert.assertEquals(anomalyType, obj.getAnomalyType());
	}

}
