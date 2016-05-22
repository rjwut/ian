package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.UnexpectedTypeException;
import com.walkertribe.ian.protocol.core.ShipActionPacket;

/**
 * Take or relinquish a bridge console.
 * @author dhleong
 */
public class SetConsolePacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_SET_CONSOLE,
				new PacketFactory() {
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
        super(TYPE_SET_CONSOLE);

        if (console == null) {
        	throw new IllegalArgumentException("You must specify a console");
        }

        mConsole = console;
        mSelected = selected;
    }

	private SetConsolePacket(PacketReader reader) {
        super(TYPE_SET_CONSOLE);
		int subtype = reader.readInt();

		if (subtype != TYPE_SET_CONSOLE) {
        	throw new UnexpectedTypeException(subtype, TYPE_SET_CONSOLE);
		}

		mConsole = Console.values()[reader.readInt()];
		mSelected = reader.readInt() == 1;
	}

	@Override
    public void writePayload(PacketWriter writer) {
    	writer	.writeInt(TYPE_SET_CONSOLE)
    			.writeInt(mConsole.ordinal())
    			.writeInt(mSelected ? 1 : 0);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mConsole).append(' ').append(mSelected ? "selected" : "deselected");
	}
}