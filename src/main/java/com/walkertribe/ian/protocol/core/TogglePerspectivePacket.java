package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Toggles between first- and third-person perspectives on the main screen. Note
 * that you cannot specify which perspective you want; you can only indicate
 * that you want to switch from the current one to the other.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.TOGGLE_PERSPECTIVE)
public class TogglePerspectivePacket extends ValueIntPacket {
	public TogglePerspectivePacket() {
		super(0);
	}

	public TogglePerspectivePacket(PacketReader reader) {
		super(reader);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}