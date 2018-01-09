package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Enables/disables keystroke capture for this console. Note that the game
 * master console always captures keystrokes; all others don't by default unless
 * this packet enables it.
 * @author rjwut
 */
public class KeyCaptureTogglePacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.KEY_CAPTURE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return KeyCaptureTogglePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new KeyCaptureTogglePacket(reader);
			}
		});
	}

	private boolean mEnabled;

	private KeyCaptureTogglePacket(PacketReader reader) {
		super(SubType.KEY_CAPTURE, reader);
		mEnabled = reader.readByte() == 1;
	}

	public KeyCaptureTogglePacket(boolean enabled) {
		super(SubType.KEY_CAPTURE);
		mEnabled = enabled;
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