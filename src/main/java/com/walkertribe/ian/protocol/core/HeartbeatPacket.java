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
import com.walkertribe.ian.protocol.core.setup.WelcomePacket;

/**
 * A packet sent periodically by the server to demonstrate that it's still
 * alive.
 * @author rjwut
 */
public class HeartbeatPacket extends BaseArtemisPacket {
	private static final PacketType TYPE = CorePacketType.HEARTBEAT;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return WelcomePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new HeartbeatPacket(reader);
			}
		});
	}

	private HeartbeatPacket(PacketReader reader) {
		super(ConnectionType.SERVER, TYPE);
	}

	public HeartbeatPacket() {
		super(ConnectionType.SERVER, TYPE);
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		// no payload
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// nothing to write
	}

}
