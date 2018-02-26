package com.walkertribe.ian.protocol;

import com.walkertribe.ian.enums.Origin;

/**
 * Any packet received for which no packet listeners have been registered will
 * be returned as this class. Only Debuggers are notified of these packets.
 * @author rjwut
 */
public final class UnparsedPacket extends RawPacket {
	public UnparsedPacket(Origin origin, int type, byte[] payload) {
		super(origin, type, payload);
	}
}