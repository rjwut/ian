package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;

/**
 * A packet sent periodically by the server to demonstrate that it's still
 * alive.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.HEARTBEAT)
public class ServerHeartbeatPacket extends HeartbeatPacket {
	public ServerHeartbeatPacket() {
	}

	public ServerHeartbeatPacket(PacketReader reader) {
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		// no payload
	}
}
