package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;

/**
 * Can be sent by the client to request a full update to the engineering grid.
 * The server will respond with an EngGridUpdatePacket.
 */
public class EngRequestGridUpdatePacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.REQUEST_ENG_GRID_UPDATE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return EngRequestGridUpdatePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new EngRequestGridUpdatePacket(reader);
			}
		});
	}

    public EngRequestGridUpdatePacket() {
        super(SubType.REQUEST_ENG_GRID_UPDATE, 0);
    }

    private EngRequestGridUpdatePacket(PacketReader reader) {
    	super(reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}