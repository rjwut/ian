package com.walkertribe.ian.protocol;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.walkertribe.ian.iface.PacketFactoryRegistry;

/**
 * An abstract Protocol implementation which provides a method to register
 * PacketFactories by providing an array of ArtemisPacket subclasses to
 * register. It assumes a convention that each packet class has a public void
 * static method named "register" that accepts a PacketFactoryRegistry object,
 * which the method will use to register one or more PacketFactory instances to
 * handle that class.
 * @author rjwut
 */
public abstract class AbstractProtocol implements Protocol {
	/**
	 * Registers PacketFactories for an array of packet classes. 
	 */
	protected static void registerPacketFactories(PacketFactoryRegistry registry,
			Class<?>[] packetClasses) {
		if (registry == null) {
			throw new IllegalArgumentException("You must provide a registry");
		}

		if (packetClasses == null) {
			throw new IllegalArgumentException("You must provide an array of classes");
		}

		for (Class<?> clazz : packetClasses) {
			Method method;

			try {
				method = clazz.getMethod("register", PacketFactoryRegistry.class);
			} catch (NoSuchMethodException ex) {
				throw new IllegalArgumentException(
						"Class " + clazz +
						" has no visible register(PacketFactoryRegistry) method",
						ex
				);
			}

			int modifiers = method.getModifiers();

			if (!Modifier.isStatic(modifiers)) {
				throw new IllegalArgumentException("Method " + method +
						" must be static");
			}

			if (!Void.TYPE.equals(method.getReturnType())) {
				throw new IllegalArgumentException("Method " + method +
						" must return void");
			}

			try {
				method.invoke(null, registry);
			} catch (IllegalArgumentException ex) {
				throw new RuntimeException(ex);
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
