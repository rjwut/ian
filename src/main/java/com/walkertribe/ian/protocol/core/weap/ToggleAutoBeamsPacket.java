package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Toggles auto beams on/off.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.TOGGLE_AUTO_BEAMS)
public class ToggleAutoBeamsPacket extends ValueIntPacket {
	public ToggleAutoBeamsPacket() {
		super(0);
	}

	public ToggleAutoBeamsPacket(PacketReader reader) {
		super(reader);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}