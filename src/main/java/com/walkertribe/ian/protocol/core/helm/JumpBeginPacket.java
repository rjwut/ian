package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Indicates that a jump has begun.
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.JUMP_BEGIN)
public class JumpBeginPacket extends SimpleEventPacket {
    public JumpBeginPacket() {
    }

    public JumpBeginPacket(PacketReader reader) {
        super(reader);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}
