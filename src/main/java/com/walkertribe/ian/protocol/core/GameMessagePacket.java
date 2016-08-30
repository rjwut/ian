package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;

/**
 * "Toast" messages sent by the server.
 */
public class GameMessagePacket extends BaseArtemisPacket {
    private static final int TYPE = 0xf754c8fe;
    private static final byte MSG_TYPE = 0x0a;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, MSG_TYPE,
				new PacketFactory() {
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

    private final String mMessage;

    private GameMessagePacket(PacketReader reader) {
        super(ConnectionType.SERVER, TYPE);
        reader.skip(4); // subtype
        mMessage = reader.readString();
    }

    public GameMessagePacket(String message) {
        super(ConnectionType.SERVER, TYPE);
        mMessage = message;
    }

    /**
     * The contents of the "toast" message.
     */
    public String getMessage() {
        return mMessage;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(MSG_TYPE).writeString(mMessage);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mMessage);
	}
}