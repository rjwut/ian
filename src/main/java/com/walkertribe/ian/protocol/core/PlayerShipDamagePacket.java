package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

public class PlayerShipDamagePacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.PLAYER_SHIP_DAMAGE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return PlayerShipDamagePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new PlayerShipDamagePacket(reader);
			}
		});
	}

    private byte[] unknown0;
    private byte[] unknown1;

    private PlayerShipDamagePacket(PacketReader reader) throws ArtemisPacketException {
        super(SubType.PLAYER_SHIP_DAMAGE, reader);
        unknown0 = reader.readBytes(4);
        unknown1 = reader.readBytes(4);
    }

    public PlayerShipDamagePacket() {
        super(SubType.PLAYER_SHIP_DAMAGE);
        unknown0 = new byte[] { 0, 0, 0, 0 };
        unknown1 = unknown0;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeBytes(unknown0).writeBytes(unknown1);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}