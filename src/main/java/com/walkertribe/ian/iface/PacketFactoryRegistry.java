package com.walkertribe.ian.iface;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CoreArtemisProtocol;
import com.walkertribe.ian.util.TextUtil;

/**
 * A registry of PacketFactories. This class handles the job of selecting the
 * appropriate PacketFactory for a given type and subType.
 * 
 * Packet type can be specified with an int or with an object (preferably an
 * enum( which implements the PacketType interface. Packet types are identified
 * in the Artemis protocol by transmitting a hash of the packet type's internal
 * name. The Artemis community has reverse-engineered the names of these packet
 * types through brute force cracking the hashes. As these names are more
 * meaningful than the corresponding hashes, it is encouraged that they be used
 * in source code when possible. However, in the event that a packet type
 * arises whose internal name is still unknown,
 * PacketFactoryRegistry.register() also supports using an int value instead.
 *
 * A packet type may optionally have subtypes. If no subtypes are specified 
 * Packet types and subtypes can be specified numerically or with enum values.
 * Packet type enums must implement the PacketType interface so they can
 * generate the int hash values that are used to identify them. Subtypes are
 * optional; if specified with an enum value, its ordinal value is used as the
 * subtype.
 * 
 * Every PacketFactoryRegistry is pre-seeded with factories from
 * {@link CoreArtemisProtocol}.
 * 
 * @author rjwut
 */
public class PacketFactoryRegistry {
	private Map<Key, PacketFactory> serverMap = new LinkedHashMap<Key, PacketFactory>();
	private Map<Key, PacketFactory> clientMap = new LinkedHashMap<Key, PacketFactory>();

	public PacketFactoryRegistry() {
		new CoreArtemisProtocol().registerPacketFactories(this);
	}

	/**
	 * Registers the given PacketFactory under the indicated packet type and
	 * subtype. This is the main registration method; all the others are
	 * convenience methods to allow using objects instead of magic number or
	 * omit the subtype argument.
	 */
	public void register(ConnectionType connType, int pktType, Byte pktSubtype,
			PacketFactory factory) {
		if (connType == null) {
			throw new IllegalArgumentException("You must provide a ConnectionType");
		}

		if (factory == null) {
			throw new IllegalArgumentException("You must provide a PacketFactory");
		}

		Map<Key, PacketFactory> map = connType == ConnectionType.SERVER ? serverMap : clientMap;
		map.put(new Key(pktType, pktSubtype), factory);
	}

	/**
	 * Registers the given PacketFactory under the indicated type.
	 */
	public void register(ConnectionType connType, PacketType pktType, PacketFactory factory) {
		register(connType, pktType.getHash(), (Byte) null, factory);
	}

	/**
	 * Registers the given PacketFactory under the indicated type.
	 */
	public void register(ConnectionType connType, int pktType, PacketFactory factory) {
		register(connType, pktType, (Byte) null, factory);
	}

	/**
	 * Registers the given PacketFactory under the indicated packet type and
	 * subtype.
	 */
	public void register(ConnectionType connType, PacketType pktType, Byte pktSubtype,
			PacketFactory factory) {
		register(connType, pktType.getHash(), pktSubtype, factory);
	}

	/**
	 * Registers the given PacketFactory under the indicated packet type and
	 * subtype.
	 */
	public void register(ConnectionType connType, int pktType, Enum<?> pktSubtype,
			PacketFactory factory) {
		Byte byteSubtype = pktSubtype == null ? null : Byte.valueOf((byte) pktSubtype.ordinal());
		register(connType, pktType, byteSubtype, factory);
	}

	/**
	 * Registers the given PacketFactory under the indicated packet type and
	 * subtype.
	 */
	public void register(ConnectionType connType, PacketType pktType, Enum<?> pktSubtype,
			PacketFactory factory) {
		register(connType, pktType.getHash(), (byte) pktSubtype.ordinal(), factory);
	}

	/**
	 * Returns the PacketFactory registered under the given ConnectionType,
	 * packet type and subtype, or null if no such PacketFactory has been
	 * registered. If a subtype is specified by to matching PacketFactory
	 * is found, the PacketFactoryRegistry will try looking for a PacketFactory
	 * for that type with no subtype. (Thus you can specify "catch all"
	 * PacketFactories that can handle any not specified by subtypes.)
	 */
	public PacketFactory get(ConnectionType connType, int pktType,
			Byte pktSubtype) {
		Map<Key, PacketFactory> map = connType == ConnectionType.SERVER ? serverMap : clientMap;

		if (pktSubtype != null) {
			// Try the subtype first
			Key key = new Key(pktType, pktSubtype);
			PacketFactory factory = map.get(key);

			if (factory != null) {
				return factory;
			}
		}

		// No subtype given, or no factory found for subtype; try no subtype
		Key key = new Key(pktType, null);
		return map.get(key);
	}


	/**
	 * Entries in the registry are stored in a Map using this class as the key.
	 * @author rjwut
	 */
	private class Key {
		private int pktType;
		private Byte pktSubtype;
		private int hashCode;

		private Key(int pktType, Byte pktSubtype) {
			this.pktType = pktType;
			this.pktSubtype = pktSubtype;
			hashCode = Objects.hash(Integer.valueOf(pktType), pktSubtype);
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
			return pktType == that.pktType && Objects.equals(pktSubtype, that.pktSubtype);
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		public String toString() {
			return TextUtil.intToHexLE(pktType) + ":" +
					(pktSubtype != null ? TextUtil.byteToHex(pktSubtype.byteValue()) : "--");
		}
	}
}