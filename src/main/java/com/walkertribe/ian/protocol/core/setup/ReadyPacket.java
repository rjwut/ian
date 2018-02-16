package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;

/**
 * Signals to the server that this console is ready to join the game. If the
 * ReadyPacket is sent before the game has started, the server will start
 * sending updates when the game starts. If the ReadyPacket is sent after the
 * game has started, the server sends updates immediately. Once a game has
 * ended, the client must send another ReadyPacket before it will be sent
 * updates again.
 * @author dhleong
 */
public class ReadyPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.READY, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return ReadyPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new ReadyPacket(reader);
			}
		});
	}

    public ReadyPacket() {
        super(SubType.READY, 0);
    }

    private ReadyPacket(PacketReader reader) {
    	super(reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}