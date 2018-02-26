package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Indicates that a jump has ended. Note that there is still some cooldown time
 * after the jump (about 5 seconds).
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.JUMP_END)
public class JumpEndPacket extends SimpleEventPacket {
    public JumpEndPacket() {
    }

    public JumpEndPacket(PacketReader reader) {
        super(reader);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}
