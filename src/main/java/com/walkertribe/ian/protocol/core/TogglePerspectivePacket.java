package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Toggles between first- and third-person perspectives on the main screen. Note
 * that you cannot specify which perspective you want; you can only indicate
 * that you want to switch from the current one to the other.
 * @author rjwut
 */
public class TogglePerspectivePacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.TOGGLE_PERSPECTIVE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return TogglePerspectivePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new TogglePerspectivePacket(reader);
			}
		});
	}

	public TogglePerspectivePacket() {
		super(SubType.TOGGLE_PERSPECTIVE, 0);
	}

	private TogglePerspectivePacket(PacketReader reader) {
		super(reader);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}