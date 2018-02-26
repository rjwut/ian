package com.walkertribe.ian.protocol;

import com.walkertribe.ian.iface.PacketFactory;

/**
 * Interface for classes which provide support for a set of packets
 * @author rjwut
 */
public interface Protocol {
	/**
	 * If this Protocol supports packets with the given type (and optional
	 * subtype), returns a PacketFactory that can parse it; otherwise returns
	 * null.
	 */
	public PacketFactory<? extends ArtemisPacket> getFactory(int type, Byte subtype);
}