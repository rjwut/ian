package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Provides intel on another vessel, typically as the result of a level 2 scan.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.OBJECT_TEXT)
public class IntelPacket extends BaseArtemisPacket {
	private final int mId;
	private final CharSequence mIntel;

	public IntelPacket(ArtemisObject obj, CharSequence intel) {
		this(obj.getId(), intel);
	}

	public IntelPacket(int hullId, CharSequence intel) {
		mId = hullId;
		mIntel = intel;
	}

	public IntelPacket(PacketReader reader) {
    	mId = reader.readInt();
    	reader.readUnknown("Unknown", 1);
        mIntel = reader.readString();
    }

	/**
	 * The ID of the ship in question
	 */
	public int getId() {
		return mId;
	}

	/**
	 * The intel on that ship, as human-readable text
	 */
	public CharSequence getIntel() {
		return mIntel;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mId).writeByte((byte) 3).writeString(mIntel);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("Obj #").append(mId).append(": ").append(mIntel);
	}
}