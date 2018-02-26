package com.walkertribe.ian.protocol;

import com.walkertribe.ian.enums.Origin;

/**
 * Any packet received that isn't of a type recognized by a registered protocol
 * will be returned as this class. If you disable packet parsing (by calling
 * ThreadedArtemisNetworkInterface.setParsePackets(false)), all packets will be
 * of this type. In most cases, you won't be interested in these: they're mainly
 * intended for reverse-engineering of the protocol and debugging.
 * @author rjwut
 */
public class UnknownPacket extends RawPacket {
	public UnknownPacket(Origin origin, int type, byte[] payload) {
		super(origin, type, payload);
	}
}