package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;
import com.walkertribe.ian.util.Util;

/**
 * "Toast" messages sent by the server.
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.GAME_MESSAGE)
public class GameMessagePacket extends SimpleEventPacket {
    private final CharSequence mMessage;

    public GameMessagePacket(CharSequence message) {
        if (Util.isBlank(message)) {
        	throw new IllegalArgumentException("You must provide a message");
        }

        mMessage = message;
    }

    public GameMessagePacket(PacketReader reader) {
        super(reader);
        mMessage = reader.readString();
    }

    /**
     * The contents of the "toast" message.
     */
    public CharSequence getMessage() {
        return mMessage;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeString(mMessage);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mMessage);
	}
}