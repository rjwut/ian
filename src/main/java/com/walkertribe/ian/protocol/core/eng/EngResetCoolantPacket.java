package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Resets coolant level on all systems to zero. This is sent by the engineering
 * console in response to the user pressing double SPACE or ENTER.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.ENG_RESET_COOLANT)
public class EngResetCoolantPacket extends BaseArtemisPacket {
	public EngResetCoolantPacket() {
	}

	public EngResetCoolantPacket(PacketReader reader) {
		reader.skip(4); // subtype
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(SubType.ENG_RESET_COOLANT);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}
