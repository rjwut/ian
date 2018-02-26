package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.util.JamCrc;
import com.walkertribe.ian.util.TextUtil;

/**
 * Sent by the client whenever the game master clicks a custom on-screen button.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.GM_BUTTON_CLICK)
public class GameMasterButtonClickPacket extends BaseArtemisPacket {
	private int mUnknown = 0x0d;
	private int mHash;

	/**
	 * Creates a click command packet for the button with the given label.
	 */
	public GameMasterButtonClickPacket(CharSequence label) {
		this(JamCrc.compute(label));
	}

	/**
	 * Creates a click command packet for the button with the given label hash.
	 */
	public GameMasterButtonClickPacket(int hash) {
        mHash = hash;
	}

	public GameMasterButtonClickPacket(PacketReader reader) {
		reader.skip(4); // subtype
		mUnknown = reader.readInt();
		mHash = reader.readInt();
	}

	/**
	 * Returns the label hash for the button that was clicked.
	 */
	public int getHash() {
		return mHash;
	}

    @Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(SubType.GM_BUTTON_CLICK);
		writer.writeInt(mUnknown);
		writer.writeInt(mHash);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("hash=").append(TextUtil.intToHexLE(mHash));
	}
}
