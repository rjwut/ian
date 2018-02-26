package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Signals to the server that this console is ready to join the game. If the
 * ReadyPacket is sent before the game has started, the server will start
 * sending updates when the game starts. If the ReadyPacket is sent after the
 * game has started, the server sends updates immediately. Once a game has
 * ended, the client must send another ReadyPacket before it will be sent
 * updates again.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.READY)
public class ReadyPacket extends ValueIntPacket {
    public ReadyPacket() {
        super(0);
    }

    public ReadyPacket(PacketReader reader) {
    	super(reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}