package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Sends a keystroke to the server. This should only be done for the game master
 * console, or if keystroke capture has been enabled via the
 * KeyCaptureTogglePacket. The Java AWT
 * <a href="https://docs.oracle.com/javase/6/docs/api/java/awt/event/KeyEvent.html">KeyEvent class</a>
 * has constants for the keycodes used by this class.
 * @author rjwut
 */
public class KeystrokePacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.KEYSTROKE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return KeystrokePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new KeystrokePacket(reader);
			}
		});
	}

	public KeystrokePacket(int keycode) {
		super(SubType.KEYSTROKE, keycode);
	}

	private KeystrokePacket(PacketReader reader) {
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