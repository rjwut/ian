package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;

/**
 * Indicates that a jump has ended. Note that there is still some cooldown time
 * after the jump (about 5 seconds).
 */
public class JumpEndPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.JUMP_END, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return JumpEndPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new JumpEndPacket(reader);
			}
		});
	}

    private JumpEndPacket(PacketReader reader) {
        super(SubType.JUMP_END, reader);
    }

    public JumpEndPacket() {
        super(SubType.JUMP_END);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}
