package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Instructions for the game master, displayed when the game master clicks the
 * "Instructions" button at the upper-left of the stock client.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.GM_BUTTON, subtype = 0x63)
public class GameMasterInstructionsPacket extends BaseArtemisPacket {
	private CharSequence mTitle;
	private CharSequence mContent;

	/**
	 * Creates new instructions for the game master.
	 */
	public GameMasterInstructionsPacket(String title, String content) {
		mTitle = title;
		mContent = content;
	}

	public GameMasterInstructionsPacket(PacketReader reader) {
        reader.skip(1); // subtype
        mTitle = reader.readString();
        mContent = reader.readString();
	}

	/**
	 * The title to display above the instructions.
	 */
	public CharSequence getTitle() {
		return mTitle;
	}

	/**
	 * The actual body text of the instructions.
	 */
	public CharSequence getContent() {
		return mContent;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer
			.writeByte((byte) 0x63) // subtype
			.writeString(mTitle)
			.writeString(mContent);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mTitle).append('\n').append(mContent);
	}
}
