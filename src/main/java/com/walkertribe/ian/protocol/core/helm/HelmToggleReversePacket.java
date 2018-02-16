package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;

/**
 * Toggles reverse engines.
 * @author dhleong
 */
public class HelmToggleReversePacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.TOGGLE_REVERSE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return HelmToggleReversePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new HelmToggleReversePacket(reader);
			}
		});
	}

    public HelmToggleReversePacket() {
        super(SubType.TOGGLE_REVERSE, 0);
    }

    private HelmToggleReversePacket(PacketReader reader) {
        super(reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}