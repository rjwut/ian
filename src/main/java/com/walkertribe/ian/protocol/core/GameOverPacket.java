package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Sent by the server when the game ends.
 * @author rjwut
 */
public class GameOverPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.GAME_OVER, new PacketFactory() {
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
    	super(SubType.GAME_OVER, reader);
    }

    public GameOverPacket() {
    	super(SubType.GAME_OVER);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}