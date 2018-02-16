package com.walkertribe.ian.iface;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Contains all the information needed to invoke a listener Method (annotated
 * with {@link Listener}).
 * @author rjwut
 */
public class ListenerMethod {
	private static final Class<?>[] ARGUMENT_TYPES = {
			ArtemisPacket.class,
			ArtemisObject.class,
			ConnectionEvent.class
	};

	private Object object;
	private Method method;
	private Class<?> paramType;

	/**
	 * @param object The listener object
	 * @param method The annotated method
	 */
	ListenerMethod (Object object, Method method) {
		validate(method);
		this.object = object;
		this.method = method;
		paramType = method.getParameterTypes()[0];
	}

	/**
	 * Throws an IllegalArgumentException if the given method is not a valid
	 * listener method.
	 */
	private static void validate(Method method) {
		Class<?> declaringClass = method.getDeclaringClass();

		if (!Modifier.isPublic(declaringClass.getModifiers())) {
			throw new IllegalArgumentException(
					"Class " + declaringClass.getName() +
					" must be public to have listener methods"
			);
		}

		if (!Modifier.isPublic(method.getModifiers())) {
			throw new IllegalArgumentException(
					"Method " + method.getName() +
					" must be public to be a listener"
			);
		}

		if (!Void.TYPE.equals(method.getReturnType())) {
			throw new IllegalArgumentException(
					"Method " + method.getName() +
					" must return void to be a listener"
			);
		}

		Class<?>[] paramTypes = method.getParameterTypes();

		if (paramTypes.length != 1) {
			throw new IllegalArgumentException(
					"Method " + method.getName() +
					" must have exactly one argument to be a listener"
			);
		}

		Class<?> paramType = paramTypes[0];

		for (Class<?> clazz : ARGUMENT_TYPES) {
			if (clazz.isAssignableFrom(paramType)) {
				return;
			}
		}
		
		throw new IllegalArgumentException(
				"Method " + method.getName() +
				" argument must be assignable to ArtemisPacket," +
				" or ConnectionEvent"
		);
	}

	/**
	 * Returns true if this ListenerMethod accepts events or packets of the
	 * given class; false otherwise.
	 */
	boolean accepts(Class<?> clazz) {
		return paramType.isAssignableFrom(clazz);
	}

	/**
	 * If the indicated argument is type-compatible with the Method's argument,
	 * the method will be invoked using that argument. Otherwise, nothing
	 * happens. Since the listeners have been pre-validated, no exception should
	 * occur, so we wrap the ones thrown by Method.invoke() in a
	 * RuntimeException.
	 */
	void offer(Object arg) {
		Class<?> clazz = arg.getClass();

		if (paramType.isAssignableFrom(clazz)) {
    		try {
				method.invoke(object, arg);
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}