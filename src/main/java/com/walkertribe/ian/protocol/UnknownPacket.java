package com.walkertribe.ian.protocol;

import com.walkertribe.ian.enums.ConnectionType;

/**
 * Any packet received that isn't of a type recognized by a registered protocol
 * will be returned as this class. If you disable packet parsing (by calling
 * ThreadedArtemisNetworkInterface.setParsePackets(false)), all packets will be
 * of this type. In most cases, you won't be interested in these: they're mainly
 * intended for reverse-engineering of the protocol and debugging. However, if
 * you are writing a server proxy, you will want to capture these so that you
 * can pass them along.
 * @author rjwut
 */
public class UnknownPacket extends RawPacket {
	public UnknownPacket(ConnectionType connectionType, int packetType,
			byte[] payload) {
		super(connectionType, packetType, payload);
	}
}