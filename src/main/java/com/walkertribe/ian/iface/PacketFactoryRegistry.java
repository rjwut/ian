package com.walkertribe.ian.iface;

import java.util.LinkedList;
import java.util.List;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.core.CoreArtemisProtocol;

/**
 * A registry of PacketFactories. This class handles the job of selecting the
 * appropriate PacketFactory for a given type and subType. PacketFactories are
 * checked in the order in which they were registered. Therefore, registering
 * the most frequently-encountered PacketFactories first will result in better
 * performance.
 * 
 * Every PacketFactoryRegistry is pre-seeded with factories from
 * {@link CoreArtemisProtocol}.
 * 
 * @author rjwut
 */
public class PacketFactoryRegistry {
	/**
	 * An entry in the PacketFactoryRegistry.
	 */
	private class Entry {
		private int pktType;
		private Byte pktSubtype;
		private PacketFactory factory;

		private Entry(int pktType, PacketFactory factory) {
			this.pktType = pktType;
			this.factory = factory;
		}

		private Entry(int pktType, byte pktSubtype, PacketFactory factory) {
			this.pktType = pktType;
			this.pktSubtype = Byte.valueOf(pktSubtype);
			this.factory = factory;
		}

		/**
		 * Returns true if this Entry corresponds to the offered type and
		 * subType; false otherwise.
		 */
		private boolean match(int offeredPktType, byte offeredPktSubtype) {
			if (pktType != offeredPktType) {
				return false;
			}

			return pktSubtype == null || pktSubtype.byteValue() == offeredPktSubtype;
		}
	}

	private List<Entry> serverEntries = new LinkedList<Entry>();
	private List<Entry> clientEntries = new LinkedList<Entry>();

	public PacketFactoryRegistry() {
		new CoreArtemisProtocol().registerPacketFactories(this);
	}

	/**
	 * Registers the given PacketFactory under the indicated type.
	 */
	public void register(ConnectionType connType, int pktType, PacketFactory factory) {
		if (connType == null) {
			throw new IllegalArgumentException("You must provide a ConnectionType");
		}

		if (factory == null) {
			throw new IllegalArgumentException("You must provide a PacketFactory");
		}

		List<Entry> list = connType == ConnectionType.SERVER ? serverEntries : clientEntries;
		list.add(new Entry(pktType, factory));
	}

	/**
	 * Registers the given PacketFactory under the indicated packet type and
	 * subtype.
	 */
	public void register(ConnectionType connType, int pktType, byte pktSubtype,
			PacketFactory factory) {
		if (connType == null) {
			throw new IllegalArgumentException("You must provide a ConnectionType");
		}

		if (factory == null) {
			throw new IllegalArgumentException("You must provide a PacketFactory");
		}

		List<Entry> list = connType == ConnectionType.SERVER ? serverEntries : clientEntries;
		list.add(new Entry(pktType, pktSubtype, factory));
	}

	/**
	 * Returns the PacketFactory registered under the given ConnectionType,
	 * packet type and subtype, or null if no such PacketFactory has been
	 * registered. If a packetFactory was not registered under a subtype, the
	 * subtype will be ignored when matching on it.
	 */
	public PacketFactory get(ConnectionType connType, int pktType,
			byte pktSubtype) {
		List<Entry> list = connType == ConnectionType.SERVER ? serverEntries : clientEntries;

		for (Entry entry : list) {
			if (entry.match(pktType, pktSubtype)) {
				return entry.factory;
			}
		}

		return null;
	}
}