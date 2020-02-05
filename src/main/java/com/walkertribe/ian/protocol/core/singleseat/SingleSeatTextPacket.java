package com.walkertribe.ian.protocol.core.singleseat;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Sent to a single-seat craft console when comms or the GM sends them a freeform text message.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.SINGLE_SEAT_TEXT)
public class SingleSeatTextPacket extends SimpleEventPacket {
	private int mId;
	private CharSequence mMessage;

	public SingleSeatTextPacket(int id, CharSequence message) {
		mId = id;
		mMessage = message;
	}

	public SingleSeatTextPacket(PacketReader reader) {
		super(reader);
		mId = reader.readInt();
		mMessage = reader.readString();
	}

	/**
	 * The ID of the single-seat craft receiving the message.
	 */
	public int getId() {
		return mId;
	}

	/**
	 * The text of the message.
	 */
	public CharSequence getMessage() {
		return mMessage;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeInt(mId).writeString(mMessage);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mId).append(": ").append(mMessage);
	}

}
