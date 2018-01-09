package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;

/**
 * Take or relinquish a bridge console.
 * @author dhleong
 */
public class SetConsolePacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SET_CONSOLE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SetConsolePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SetConsolePacket(reader);
			}
		});
	}

	private Console mConsole;
	private boolean mSelected;

	/**
	 * @param console The Console being updated
	 * @param selected Whether the player is taking this console or not
	 */
	public SetConsolePacket(Console console, boolean selected) {
        super(SubType.SET_CONSOLE);

        if (console == null) {
        	throw new IllegalArgumentException("You must specify a console");
        }

        mConsole = console;
        mSelected = selected;
    }

	private SetConsolePacket(PacketReader reader) {
        super(SubType.SET_CONSOLE);
        reader.skip(4); // subtype
		mConsole = Console.values()[reader.readInt()];
		mSelected = reader.readInt() == 1;
	}

	@Override
    public void writePayload(PacketWriter writer) {
    	writer	.writeInt(SubType.SET_CONSOLE.ordinal())
    			.writeInt(mConsole.ordinal())
    			.writeInt(mSelected ? 1 : 0);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mConsole).append(' ').append(mSelected ? "selected" : "deselected");
	}
}