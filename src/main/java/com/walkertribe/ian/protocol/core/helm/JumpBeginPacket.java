package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Indicates that a jump has begun.
 */
public class JumpBeginPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.SIMPLE_EVENT;
    private static final byte SUBTYPE = 0x0c;

	public static void register(PacketFactoryRegistry registry) {
		PacketFactory factory = new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return JumpBeginPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new JumpBeginPacket(reader);
			}
		};
		registry.register(ConnectionType.SERVER, TYPE, SUBTYPE, factory);
	}

    private JumpBeginPacket(PacketReader reader) {
        super(ConnectionType.SERVER, TYPE);
        reader.skip(4); // subtype
    }

    public JumpBeginPacket() {
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
