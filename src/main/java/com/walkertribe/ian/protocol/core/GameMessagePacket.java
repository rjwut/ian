package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * "Toast" messages sent by the server.
 */
public class GameMessagePacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.GAME_MESSAGE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameMessagePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameMessagePacket(reader);
			}
		});
	}

    private final CharSequence mMessage;

    private GameMessagePacket(PacketReader reader) {
        super(SubType.GAME_MESSAGE, reader);
        mMessage = reader.readString();
    }

    public GameMessagePacket(String message) {
        super(SubType.GAME_MESSAGE);

        if (message == null || message.length() == 0) {
        	throw new IllegalArgumentException("You must provide a message");
        }

        mMessage = message;
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