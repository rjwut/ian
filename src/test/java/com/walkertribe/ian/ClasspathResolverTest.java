package com.walkertribe.ian;

import org.junit.Test;

public class ClasspathResolverTest {
	@Test(expected = IllegalArgumentException.class)
	public void testNullClass() {
		new ClasspathResolver(null);
	}
}
