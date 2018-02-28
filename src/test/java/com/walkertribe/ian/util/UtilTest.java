package com.walkertribe.ian.util;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest {
	private enum TestEnum {
		FOO, BAR, BAZ, FOOBAR
	}

	private static final Set<TestEnum> SET = new LinkedHashSet<TestEnum>();

	static {
		SET.add(TestEnum.FOO);
		SET.add(TestEnum.BAR);
		SET.add(TestEnum.BAZ);
	}

	@Test
	public void testContainsAny() {
		Assert.assertTrue(Util.containsAny(SET, TestEnum.FOO));
		Assert.assertTrue(Util.containsAny(SET, TestEnum.BAR));
		Assert.assertTrue(Util.containsAny(SET, TestEnum.BAZ));
		Assert.assertTrue(Util.containsAny(SET, TestEnum.FOO, TestEnum.BAR, TestEnum.BAZ));
		Assert.assertFalse(Util.containsAny(SET, TestEnum.FOOBAR));
		Assert.assertFalse(Util.containsAny(SET));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testContainsAnyNullCollection() {
		Util.containsAny(null, TestEnum.FOO);
	}

	@Test
	public void testEnumSetToString() {
		Assert.assertEquals("", Util.enumSetToString(new LinkedHashSet<TestEnum>()));
		Assert.assertEquals("FOO BAR BAZ", Util.enumSetToString(SET));
	}

	@Test
	public void testEnumSetToStringNullSet() {
		Assert.assertEquals("", Util.enumSetToString(null));
	}

	@Test
	public void makeEclEmmaHappy() {
		TestUtil.coverPrivateConstructor(Util.class);
	}
}
