package com.walkertribe.ian.protocol;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.util.TextUtil;

/**
 * An abstract Protocol implementation which provides a mechanism to register
 * packet classes with the Packet annotation and retrieve them again. It
 * assumes that every packet has a constructor which takes a PacketReader.
 * @author rjwut
 */
public abstract class AbstractProtocol implements Protocol {
	Map<Key, Factory<?>> registry = new HashMap<Key, Factory<?>>();

	/**
	 * Invoked by the Protocol implementation to register a single packet
	 * class. The class must have a Packet annotation and an accessible
	 * constructor with a single PacketReader argument.
	 */
	protected <T extends ArtemisPacket> void register(Class<T> clazz) {
		Factory<?> factory;

		try {
			factory = new Factory<T>(clazz);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex); // shouldn't happen
		}

		Packet anno = clazz.getAnnotation(Packet.class);

		if (anno == null) {
			throw new IllegalArgumentException(clazz + " has no @Packet annotation");
		}

		int type = BaseArtemisPacket.getHash(anno);
		byte[] subtypes = anno.subtype();

		if (subtypes.length == 0) {
			registry.put(new Key(type, null), factory);
		} else {
			for (byte subtype : subtypes) {
				registry.put(new Key(type, subtype), factory);
			}
		}
	}

	@Override
	public PacketFactory<?> getFactory(int type, Byte subtype) {
		PacketFactory<?> factory = registry.get(new Key(type, subtype));

		if (factory == null && subtype != null) {
			// no factory found for that subtype; try without subtype
			factory = registry.get(new Key(type, null));
		}

		return factory;
	}

	/**
	 * Entries in the registry are stored in a Map using this class as the key.
	 * @author rjwut
	 */
	private class Key {
		private int type;
		private Byte subtype;
		private int hashCode;

		/**
		 * Creates a new Key for this type and subtype.
		 */
		private Key(int type, Byte subtype) {
			this.type = type;
			this.subtype = subtype;
			hashCode = Objects.hash(Integer.valueOf(type), subtype);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Key)) {
				return false;
			}

			Key that = (Key) obj;
			return type == that.type && Objects.equals(subtype, that.subtype);
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		@Override
		public String toString() {
			return TextUtil.intToHexLE(type) + ":" +
					(subtype != null ? TextUtil.byteToHex(subtype.byteValue()) : "--");
		}
	}

	/**
	 * PacketFactory implementation that invokes a constructor with a
	 * PacketReader argument.
	 * @author rjwut
	 */
	private class Factory<T extends ArtemisPacket> implements PacketFactory<T> {
		private Class<T> clazz;
		private Constructor<T> constructor;

		private Factory(Class<T> clazz) throws ReflectiveOperationException {
			this.clazz = clazz;
			constructor = clazz.getDeclaredConstructor(PacketReader.class);
		}

		@Override
		public Class<T> getFactoryClass() {
			return clazz;
		}

		public T build(PacketReader reader) throws ArtemisPacketException {
			try {
				return constructor.newInstance(reader);
			} catch (InvocationTargetException ex) {
				throw new ArtemisPacketException(ex.getCause());
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
