package com.walkertribe.ian.util;

import org.junit.Assert;
import org.junit.Test;

public class BoolStateTest {
	@Test
	public void testGetBooleanValue() {
		Assert.assertTrue(BoolState.TRUE.getBooleanValue());
		Assert.assertFalse(BoolState.FALSE.getBooleanValue());
		Assert.assertFalse(BoolState.UNKNOWN.getBooleanValue());
	}

	@Test
	public void testIsKnown() {
		Assert.assertTrue(BoolState.isKnown(BoolState.TRUE));
		Assert.assertTrue(BoolState.isKnown(BoolState.FALSE));
		Assert.assertFalse(BoolState.isKnown(BoolState.UNKNOWN));
		Assert.assertFalse(BoolState.isKnown(null));
	}

	@Test
	public void testSafeValue() {
		Assert.assertTrue(BoolState.safeValue(BoolState.TRUE));
		Assert.assertFalse(BoolState.safeValue(BoolState.FALSE));
		Assert.assertFalse(BoolState.safeValue(BoolState.UNKNOWN));
		Assert.assertFalse(BoolState.safeValue(null));
	}
}
