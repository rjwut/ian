package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;

/**
 * Indicates that a jump has ended. Note that there is still some cooldown time
 * after the jump (about 5 seconds).
 */
public class JumpEndPacket extends BaseArtemisPacket {
    private static final int TYPE = 0xf754c8fe;
    private static final byte SUBTYPE = 0x0d;

	public static void register(PacketFactoryRegistry registry) {
		PacketFactory factory = new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return JumpEndPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new JumpEndPacket(reader);
			}
		};
		registry.register(ConnectionType.SERVER, TYPE, SUBTYPE, factory);
	}

    private JumpEndPacket(PacketReader reader) {
        super(ConnectionType.SERVER, TYPE);
        reader.skip(4); // subtype
    }

    public JumpEndPacket() {
        super(ConnectionType.SERVER, TYPE);
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(SUBTYPE);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}
