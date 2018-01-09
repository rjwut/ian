package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;

/**
 * Indicates that a jump has begun.
 */
public class JumpBeginPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.JUMP_BEGIN, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return JumpBeginPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new JumpBeginPacket(reader);
			}
		});
	}

    private JumpBeginPacket(PacketReader reader) {
        super(SubType.JUMP_BEGIN, reader);
    }

    public JumpBeginPacket() {
        super(SubType.JUMP_END);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}
