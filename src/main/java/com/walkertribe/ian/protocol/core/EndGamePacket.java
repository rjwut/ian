package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Sent by the server when the "End Game" button is clicked on the statistics page.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.END_GAME)
public class EndGamePacket extends SimpleEventPacket {
    public EndGamePacket() {
    }

    public EndGamePacket(PacketReader reader) {
    	super(reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}
