package com.walkertribe.ian.protocol.core.comm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Toggles red alert on and off.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.TOGGLE_RED_ALERT)
public class ToggleRedAlertPacket extends ValueIntPacket {
	public ToggleRedAlertPacket() {
        super(0);
    }

	public ToggleRedAlertPacket(PacketReader reader) {
        super(reader);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}