package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Upgrade;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisAnomalyTest {
	@Test
	public void testUpdateFrom() {
		ArtemisAnomaly obj0 = new ArtemisAnomaly(47);
		assertAnomaly(obj0, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, null);
		ArtemisAnomaly obj1 = new ArtemisAnomaly(47);
		obj1.setX(1.0f);
		obj1.setY(2.0f);
		obj1.setZ(3.0f);
		obj1.setUpgrade(Upgrade.LATERAL_ARRAY);
		obj1.updateFrom(obj0, null);
		assertAnomaly(obj1, 1.0f, 2.0f, 3.0f, Upgrade.LATERAL_ARRAY);
		obj0.updateFrom(obj1, null);
		assertAnomaly(obj0, 1.0f, 2.0f, 3.0f, Upgrade.LATERAL_ARRAY);
		obj0.updateFrom(new ArtemisBase(48), null);
	}

	public static void assertAnomaly(ArtemisAnomaly obj, float x, float y, float z, Upgrade upgrade) {
		Assert.assertEquals(x, obj.getX(), TestUtil.EPSILON);
		Assert.assertEquals(y, obj.getY(), TestUtil.EPSILON);
		Assert.assertEquals(z, obj.getZ(), TestUtil.EPSILON);
		Assert.assertEquals(upgrade, obj.getUpgrade());
	}

}
