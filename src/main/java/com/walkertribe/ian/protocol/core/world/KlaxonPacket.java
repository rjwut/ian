package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;

public class KlaxonPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.KLAXON, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return KlaxonPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new KlaxonPacket(reader);
			}
		});
	}

    private KlaxonPacket(PacketReader reader) {
        super(SubType.KLAXON, reader);
    }

    public KlaxonPacket() {
        super(SubType.KLAXON);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// nothing else to write
	}
}
