package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;

/**
 * Enables/disables keystroke capture for this console. Note that the game
 * master console always captures keystrokes; all others don't by default unless
 * this packet enables it.
 * @author rjwut
 */
public class KeyCaptureTogglePacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.SIMPLE_EVENT;
	private static final byte MSG_TYPE = 0x11;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, MSG_TYPE,
				new PacketFactory() {
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
		super(ConnectionType.SERVER, TYPE);
        reader.skip(4); // subtype
		mEnabled = reader.readByte() == 1;
	}

	public KeyCaptureTogglePacket(boolean enabled) {
		super(ConnectionType.SERVER, TYPE);
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
		writer.writeInt(MSG_TYPE).writeByte((byte) (mEnabled ? 1 : 0));
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mEnabled ? "ON" : "OFF");
	}
}