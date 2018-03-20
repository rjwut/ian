package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.IntelType;
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
	private IntelType mIntelType;
	private final CharSequence mIntel;

	public IntelPacket(ArtemisObject obj, IntelType type, CharSequence intel) {
		this(obj.getId(), type, intel);
	}

	public IntelPacket(int hullId, IntelType type, CharSequence intel) {
		mId = hullId;
		mIntelType = type;
		mIntel = intel;
	}

	public IntelPacket(PacketReader reader) {
    	mId = reader.readInt();
    	mIntelType = IntelType.values()[reader.readByte()];
        mIntel = reader.readString();
    }

	/**
	 * The ID of the ship in question
	 */
	public int getId() {
		return mId;
	}

	/**
	 * The type of intel received
	 */
	public IntelType getIntelType() {
		return mIntelType;
	}

	/**
	 * The intel on that ship, as human-readable text
	 */
	public CharSequence getIntel() {
		return mIntel;
	}

	/**
	 * Applies the intel in this packet to the given ArtemisObject.
	 */
	public void applyTo(ArtemisObject obj) {
		mIntelType.set(obj, mIntel);;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mId).writeByte((byte) mIntelType.ordinal()).writeString(mIntel);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("Obj #").append(mId).append(": ").append(mIntelType).append('=').append(mIntel);
	}
}