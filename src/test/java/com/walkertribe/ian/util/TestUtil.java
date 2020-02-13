package com.walkertribe.ian.util;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.Assert;

/**
 * Utility methods for testing. Code testing the source Util class is found in
 * the UtilTest class, not this one.
 * @author rjwut
 */
public class TestUtil {
	// Are we running in debug mode?
	public static final boolean DEBUG = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("jdwp") >= 0;
	public static final float EPSILON = 0.00000001f; // for float equality checks

	/**
	 * EclEmma will highlight the package line of an enum as uncovered if
	 * valueOf() is never invoked. It's dumb to test this because it's JVM
	 * code, not your own, but passing the enum's class into this method
	 * will appease EclEmma. This won't work for empty enums, but why would you
	 * have one of those?
	 */
	public static <T extends Enum<?>> void coverEnumValueOf(Class<T> clazz) {
		try {
			Method method = clazz.getMethod("valueOf", String.class);
			method.setAccessible(true); // valueOf() is always public, but maybe the enum isn't
			method.invoke(null, clazz.getEnumConstants()[0].name());
		} catch (ReflectiveOperationException | SecurityException | IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * It's common practice to create a private no-arg constructor on static
	 * utility classes to prevent them from being instantiated. Unfortunately
	 * EclEmma complains that the constructor is uncovered because it's never
	 * invoked. So this will invoke the given class's no-arg constructor for
	 * you via reflection to make EclEmma happy.
	 */
	public static <T> void coverPrivateConstructor(Class<T> clazz) {
		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			constructor.newInstance();
		} catch (ReflectiveOperationException | SecurityException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Performs a basic exercise of the equals() and hashCode() contracts for a
	 * class. You must provide three or more objects of the same class. The
	 * first (obj) is the test object. The second is an object which equals()
	 * obj but isn't == to it. The rest are objects which are neither == nor
	 * equals() to obj.
	 */
	public static <T> void testEqualsAndHashCode(T obj, T equal, @SuppressWarnings("unchecked") T... notEqual) {
		if (obj == null) {
			throw new IllegalArgumentException("Must specify obj");
		}

		if (equal == null) {
			throw new IllegalArgumentException("Must specify equal");
		}

		if (notEqual == null || notEqual.length == 0) {
			throw new IllegalArgumentException("Must specify notEqual");
		}

		if (obj == equal) {
			throw new IllegalArgumentException("obj == equal not allowed (it must be equal() but !=)");
		}

		for (T ne : notEqual) {
			if (ne == null) {
				throw new IllegalArgumentException("Can't give a null notEqual value");
			}

			if (obj == ne) {
				throw new IllegalArgumentException("Can't give a notEqual value that == obj");
			}

			if (equal == ne) {
				throw new IllegalArgumentException("Can't give a notEqual value that == equal");
			}
		}

		Assert.assertEquals(obj, obj);
		Assert.assertNotEquals(obj, null);
		Assert.assertNotEquals(obj, "foo");
		Assert.assertEquals(obj, equal);

		for (T ne : notEqual) {
			Assert.assertNotEquals(obj, ne);
		}

		Assert.assertEquals(obj.hashCode(), obj.hashCode());
		Assert.assertEquals(obj.hashCode(), equal.hashCode());
	}

	/**
	 * Assert that the value returned by toString() is equal() for both objects. The assertion
	 * succeeds if both Objects are null, but fails if only one is. This is useful, for instance,
	 * for checking that two CharSequences represent the same value, even if they are of different
	 * implementation classes.
	 */
	public static void assertToStringEquals(Object expected, Object actual) {
		if (expected == actual) {
			return;
		}

		if (expected == null || actual == null) {
			Assert.fail("Expected <" + expected + "> but got <" + actual + ">");
		}

		Assert.assertEquals(expected.toString(), actual.toString());
	}
}
