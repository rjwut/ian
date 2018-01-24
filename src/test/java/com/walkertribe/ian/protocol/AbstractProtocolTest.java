package com.walkertribe.ian.protocol;

import org.junit.Test;

import com.walkertribe.ian.iface.PacketFactoryRegistry;

public class AbstractProtocolTest {
	@Test
	public void test() {
		AbstractProtocol.registerPacketFactories(
				new PacketFactoryRegistry(),
				new Class<?>[] { ClassToRegister.class }
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullRegistry() {
		AbstractProtocol.registerPacketFactories(
				null,
				new Class<?>[] { ClassToRegister.class }
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullClassArray() {
		AbstractProtocol.registerPacketFactories(new PacketFactoryRegistry(), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoRegisterMethod() {
		AbstractProtocol.registerPacketFactories(
				new PacketFactoryRegistry(),
				new Class<?>[] { NoRegisterMethod.class }
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDoesntHaveRegistryArg() {
		AbstractProtocol.registerPacketFactories(
				new PacketFactoryRegistry(),
				new Class<?>[] { DoesntHaveRegistryArg.class }
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotPublic() {
		AbstractProtocol.registerPacketFactories(
				new PacketFactoryRegistry(),
				new Class<?>[] { NotPublic.class }
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotStatic() {
		AbstractProtocol.registerPacketFactories(
				new PacketFactoryRegistry(),
				new Class<?>[] { NotStatic.class }
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDoesntReturnVoid() {
		AbstractProtocol.registerPacketFactories(
				new PacketFactoryRegistry(),
				new Class<?>[] { DoesntReturnVoid.class }
		);
	}

	private static class ClassToRegister {
		@SuppressWarnings("unused")
		public static void register(PacketFactoryRegistry registry) {
		}
	}

	private static class NoRegisterMethod {
	}

	private static class DoesntHaveRegistryArg {
		@SuppressWarnings("unused")
		public static void register() {
		}
	}

	private static class NotPublic {
		@SuppressWarnings("unused")
		private static void register(PacketFactoryRegistry registry) {
		}
	}

	private static class NotStatic {
		@SuppressWarnings("unused")
		public void register(PacketFactoryRegistry registry) {
		}
	}

	private static class DoesntReturnVoid {
		@SuppressWarnings("unused")
		public static Object register(PacketFactoryRegistry registry) {
			return null;
		}
	}
}