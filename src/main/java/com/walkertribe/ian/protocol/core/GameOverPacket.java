package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Sent by the server when the simulation ends (victory, player's ship is destroyed, etc.).
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.GAME_OVER)
public class GameOverPacket extends SimpleEventPacket {
    public GameOverPacket() {
    }

    public GameOverPacket(PacketReader reader) {
    	super(reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}
