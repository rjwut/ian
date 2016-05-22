package com.walkertribe.ian.protocol;

import com.walkertribe.ian.enums.ConnectionType;

/**
 * Any packet received for which no packet listeners have been registered will
 * be returned as this class. Only Debuggers are notified of these packets.
 * @author rjwut
 */
public final class UnparsedPacket extends RawPacket {
	public UnparsedPacket(ConnectionType connectionType, int packetType,
			byte[] payload) {
		super(connectionType, packetType, payload);
	}
}