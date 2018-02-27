package com.walkertribe.ian.protocol;

import java.util.ArrayList;
import java.util.List;

import com.walkertribe.ian.iface.PacketFactory;

/**
 * Allows multiple AbstractProtocols to be combined into a single Protocol
 * implementation. Protocols added later have priority over earlier ones.
 * @author rjwut
 */
public class CompositeProtocol implements Protocol {
	private List<Protocol> list = new ArrayList<Protocol>();

	/**
	 * Adds this Protocol to the composite.
	 */
	public void add(Protocol protocol) {
		list.add(0, protocol);
	}

	@Override
	public PacketFactory<? extends ArtemisPacket> getFactory(int type, Byte subtype) {
		for (Protocol protocol : list) {
			PacketFactory<? extends ArtemisPacket> factory = protocol.getFactory(type, subtype);

			if (factory != null) {
				return factory;
			}
		}

		return null;
	}
}
