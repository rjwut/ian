package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Sends a keystroke to the server. This should only be done for the game master
 * console, or if keystroke capture has been enabled via the
 * KeyCaptureTogglePacket. The Java AWT
 * <a href="https://docs.oracle.com/javase/6/docs/api/java/awt/event/KeyEvent.html">KeyEvent class</a>
 * has constants for the keycodes used by this class.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.KEYSTROKE)
public class KeystrokePacket extends ValueIntPacket {
	public KeystrokePacket(int keycode) {
		super(keycode);
	}

	public KeystrokePacket(PacketReader reader) {
		super(reader);
	}

	/**
	 * Returns the keycode for the key that was pressed.
	 */
	public int getKeycode() {
		return mArg;
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}