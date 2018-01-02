package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;

public class PlayerShipDamagePacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.SIMPLE_EVENT;

	public static void register(PacketFactoryRegistry registry) {
		PacketFactory factory = new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return PlayerShipDamagePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new PlayerShipDamagePacket(reader);
			}
		};
		registry.register(ConnectionType.SERVER, TYPE, MSG_TYPE, factory);
	}

    public static final byte MSG_TYPE = 0x05;

    private byte[] unknown0;
    private byte[] unknown1;

    private PlayerShipDamagePacket(PacketReader reader) throws ArtemisPacketException {
        super(ConnectionType.SERVER, TYPE);
        int subtype = reader.readInt();

        if (subtype != MSG_TYPE) {
        	throw new ArtemisPacketException(
        			"Expected subtype " + MSG_TYPE + ", got " + subtype
        	);
        }

        unknown0 = reader.readBytes(4);
        unknown1 = reader.readBytes(4);
    }

    public PlayerShipDamagePacket() {
        super(ConnectionType.SERVER, TYPE);
        unknown0 = new byte[] { 0, 0, 0, 0 };
        unknown1 = unknown0;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(MSG_TYPE).writeBytes(unknown0).writeBytes(unknown1);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}