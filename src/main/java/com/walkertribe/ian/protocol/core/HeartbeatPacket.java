package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;

/**
 * A packet sent periodically by the server to demonstrate that it's still
 * alive.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.HEARTBEAT)
public class HeartbeatPacket extends BaseArtemisPacket {
	public HeartbeatPacket() {
	}

	public HeartbeatPacket(PacketReader reader) {
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		// no payload
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// nothing to write
	}

}
