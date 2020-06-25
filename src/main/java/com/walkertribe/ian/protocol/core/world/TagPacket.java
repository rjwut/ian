package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;
import com.walkertribe.ian.world.Tag;

@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.TAG)
public class TagPacket extends SimpleEventPacket {
	private int mId;
	private Tag mTag;

	public TagPacket(int id, Tag tag) {
		mId = id;
		mTag = tag;
	}

	public TagPacket(PacketReader reader) {
		reader.skip(4); // subtype
		mId = reader.readInt();
		mTag = new Tag(reader);
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
	public Tag getTag() {
		return mTag;
	}

    @Override
	protected void writePayload(PacketWriter writer) {
    	super.writePayload(writer);
		writer.writeInt(mId);
		mTag.write(writer);;
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("Object #").append(mId).append(": ").append(mTag);
	}

}
