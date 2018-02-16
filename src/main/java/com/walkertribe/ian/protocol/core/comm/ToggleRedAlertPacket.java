package com.walkertribe.ian.protocol.core.comm;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;

/**
 * Toggles red alert on and off.
 */
public class ToggleRedAlertPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.TOGGLE_RED_ALERT, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return ToggleRedAlertPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new ToggleRedAlertPacket(reader);
			}
		});
	}

	public ToggleRedAlertPacket() {
        super(SubType.TOGGLE_RED_ALERT, 0);
    }

	private ToggleRedAlertPacket(PacketReader reader) {
        super(reader);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}