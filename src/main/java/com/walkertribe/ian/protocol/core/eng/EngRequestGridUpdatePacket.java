package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Can be sent by the client to request a full update to the engineering nodes.
 * The server will respond with an EngGridUpdatePacket.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.REQUEST_ENG_GRID_UPDATE)
public class EngRequestGridUpdatePacket extends ValueIntPacket {
    public EngRequestGridUpdatePacket() {
        super(0);
    }

    public EngRequestGridUpdatePacket(PacketReader reader) {
    	super(reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}