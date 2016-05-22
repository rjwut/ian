package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ShipActionPacket;

/**
 * Request to dock. This can be issued by most player ships to request docking
 * with the nearest base. For fighters, it is a request to dock with the
 * mothership.
 * @author dhleong
 */
public class HelmRequestDockPacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_REQUEST_DOCK,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return HelmRequestDockPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new HelmRequestDockPacket(reader);
			}
		});
	}

	public HelmRequestDockPacket() {
        super(TYPE_REQUEST_DOCK, 0);
    }

	private HelmRequestDockPacket(PacketReader reader) {
        super(TYPE_REQUEST_DOCK, reader);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}