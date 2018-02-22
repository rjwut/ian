package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Notifies clients that the main screen perspective has been toggled.
 * @author rjwut
 */
public class PerspectivePacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.PERSPECTIVE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return PerspectivePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new PerspectivePacket(reader);
			}
		});
	}

	private PerspectivePacket(PacketReader reader) {
    	super(SubType.PERSPECTIVE, reader);
    }

    public PerspectivePacket() {
    	super(SubType.PERSPECTIVE);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
    	// nothing to write here
	}
}