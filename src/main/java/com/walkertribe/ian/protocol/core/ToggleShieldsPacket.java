package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Toggle shields
 * @author dhleong
 */
public class ToggleShieldsPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SubType.TOGGLE_SHIELDS,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return ToggleShieldsPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new ToggleShieldsPacket(reader);
			}
		});
	}

    public ToggleShieldsPacket() {
        super(SubType.TOGGLE_SHIELDS, 0);
    }

    private ToggleShieldsPacket(PacketReader reader) {
    	super(SubType.TOGGLE_SHIELDS, reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}