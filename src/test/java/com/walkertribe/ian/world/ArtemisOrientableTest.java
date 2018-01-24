package com.walkertribe.ian.world;

import org.junit.Assert;

import com.walkertribe.ian.util.TestUtil;

public class ArtemisOrientableTest {
	public static void assertOrientable(ArtemisOrientable obj, float heading, float pitch, float roll) {
		Assert.assertEquals(heading, obj.getHeading(), TestUtil.EPSILON);
		Assert.assertEquals(pitch, obj.getPitch(), TestUtil.EPSILON);
		Assert.assertEquals(roll, obj.getRoll(), TestUtil.EPSILON);
	}
}
