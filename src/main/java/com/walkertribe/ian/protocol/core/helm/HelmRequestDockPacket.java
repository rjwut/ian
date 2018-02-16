package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;

/**
 * Request to dock. This can be issued by most player ships to request docking
 * with the nearest base. For single-seat craft, it is a request to dock with
 * the mothership.
 * @author dhleong
 */
public class HelmRequestDockPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.REQUEST_DOCK, new PacketFactory() {
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
        super(SubType.REQUEST_DOCK, 0);
    }

	private HelmRequestDockPacket(PacketReader reader) {
        super(reader);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}