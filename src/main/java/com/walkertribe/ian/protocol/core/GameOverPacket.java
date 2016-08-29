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
 * Sent by the server when the game ends.
 * @author rjwut
 */
public class GameOverPacket extends BaseArtemisPacket {
    private static final int TYPE = 0xf754c8fe;
    private static final byte MSG_TYPE = 0x06;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, MSG_TYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameOverPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameOverPacket(reader);
			}
		});
	}

    private GameOverPacket(PacketReader reader) {
    	super(ConnectionType.SERVER, TYPE);
        reader.skip(4); // subtype
    }

    public GameOverPacket() {
    	super(ConnectionType.SERVER, TYPE);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(MSG_TYPE);
	}
}