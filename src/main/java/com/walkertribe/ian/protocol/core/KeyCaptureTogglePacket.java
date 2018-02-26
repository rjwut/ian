package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Enables/disables keystroke capture for this console. Note that the game
 * master console always captures keystrokes; all others don't by default unless
 * this packet enables it.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.KEY_CAPTURE)
public class KeyCaptureTogglePacket extends SimpleEventPacket {
	private boolean mEnabled;

	public KeyCaptureTogglePacket(boolean enabled) {
		mEnabled = enabled;
	}

	public KeyCaptureTogglePacket(PacketReader reader) {
		super(reader);
		mEnabled = reader.readByte() == 1;
	}

	/**
	 * Returns true if this console should capture keystrokes; false otherwise.
	 */
	public boolean isEnabled() {
		return mEnabled;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeByte((byte) (mEnabled ? 1 : 0));
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mEnabled ? "ON" : "OFF");
	}
}