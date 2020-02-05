package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;
import com.walkertribe.ian.util.Util;

@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.TAG)
public class TagPacket extends SimpleEventPacket {
	private static final byte[] UNKNOWN = new byte[] { 0, 0, 0, 0 };

	private int mId;
	private byte[] mUnknown;
	private CharSequence mTagger;
	private CharSequence mDate;

	public TagPacket(int id, CharSequence tagger, CharSequence date) {
		if (Util.isBlank(tagger)) {
			throw new IllegalArgumentException("You must provide a tagger name");
		}

		if (Util.isBlank(date)) {
			throw new IllegalArgumentException("You must provide a date");
		}

		mId = id;
		mUnknown = UNKNOWN;
		mTagger = tagger;
		mDate = date;
	}

	public TagPacket(PacketReader reader) {
		reader.skip(4); // subtype
		mId = reader.readInt();
		mUnknown = reader.readBytes(4);
		mTagger = reader.readString();
		mDate = reader.readString();
	}

	/**
	 * The ID of the tagged object
	 */
	public int getId() {
		return mId;
	}

	/**
	 * The name of the ship the tag belongs to
	 */
	public CharSequence getTagger() {
		return mTagger;
	}

	/**
	 * The date the tag was deployed
	 */
	public CharSequence getDate() {
		return mDate;
	}

    @Override
	protected void writePayload(PacketWriter writer) {
    	super.writePayload(writer);
		writer.writeInt(mId).writeBytes(mUnknown).writeString(mTagger).writeString(mDate);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("Object #" + mId + " tagged by ").append(mTagger).append(" on ").append(mDate);
	}

}
