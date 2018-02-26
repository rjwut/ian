package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.KLAXON)
public class KlaxonPacket extends SimpleEventPacket {
    public KlaxonPacket() {
    }

    public KlaxonPacket(PacketReader reader) {
        super(reader);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// nothing else to write
	}
}
