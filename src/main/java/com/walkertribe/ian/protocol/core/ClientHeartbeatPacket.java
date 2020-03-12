package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * A packet sent periodically by the client to demonstrate that it's still
 * alive.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.CLIENT_HEARTBEAT)
public class ClientHeartbeatPacket extends HeartbeatPacket {
    public ClientHeartbeatPacket() {
        // do nothing
    }

    public ClientHeartbeatPacket(PacketReader reader) {
        reader.skip(4); // subtype
    }

    @Override
    protected void writePayload(PacketWriter writer) {
        writer.writeInt(SubType.CLIENT_HEARTBEAT);
    }
}
