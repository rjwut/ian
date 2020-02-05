package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.Util;

/**
 * Sent by the server when someone sends a chat message in the pre-game screen.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.IDLE_TEXT)
public class IdleTextPacket extends BaseArtemisPacket {
	private CharSequence mShipName;
	private CharSequence mMessage;

	public IdleTextPacket(CharSequence shipName, CharSequence message) {
		if (Util.isBlank(shipName)) {
			throw new IllegalArgumentException("You must provide a ship name");
		}

		if (Util.isBlank(message)) {
			throw new IllegalArgumentException("You must provide a message");
		}

		mShipName = shipName;
		mMessage = message;
	}

	public IdleTextPacket(PacketReader reader) {
		mShipName = reader.readString();
		mMessage = reader.readString();
	}

	/**
	 * The name of the ship that sent the message
	 */
	public CharSequence getShipName() {
		return mShipName;
	}

	/**
	 * The content of the message
	 */
	public CharSequence getMessage() {
		return mMessage;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeString(mShipName);
		writer.writeString(mMessage);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mShipName).append(": ").append(mMessage);
	}
}
