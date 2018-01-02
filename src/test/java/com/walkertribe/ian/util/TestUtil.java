package com.walkertribe.ian.util;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

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
			method.invoke(null, clazz.getEnumConstants()[0].name());
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex);
		} catch (SecurityException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * It's common practice to create a private constructor on static utility
	 * methods to prevent them from being instantiated. Unfortunately EclEmma
	 * complains that the constructor is uncovered because it's never invoked.
	 * So this will invoke the given class's constructor for you via reflection
	 * to make EclEmma happy.
	 */
	public static <T> void coverPrivateConstructor(Class<T> clazz) {
		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			constructor.newInstance();
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex);
		} catch (SecurityException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		}
	}
}
