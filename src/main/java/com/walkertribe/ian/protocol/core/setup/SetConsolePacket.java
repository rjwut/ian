package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Take or relinquish a bridge console.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.SET_CONSOLE)
public class SetConsolePacket extends BaseArtemisPacket {
	private Console mConsole;
	private boolean mSelected;

	/**
	 * @param console The Console being updated
	 * @param selected Whether the player is taking this console or not
	 */
	public SetConsolePacket(Console console, boolean selected) {
        if (console == null) {
        	throw new IllegalArgumentException("You must specify a console");
        }

        mConsole = console;
        mSelected = selected;
    }

	public SetConsolePacket(PacketReader reader) {
        reader.skip(4); // subtype
		mConsole = Console.values()[reader.readInt()];
		mSelected = reader.readInt() == 1;
	}

	/**
	 * The Console being updated
	 */
	public Console getConsole() {
		return mConsole;
	}

	/**
	 * Returns true if the Console is selected
	 */
	public boolean isSelected() {
		return mSelected;
	}

	@Override
    public void writePayload(PacketWriter writer) {
    	writer	.writeInt(SubType.SET_CONSOLE)
    			.writeInt(mConsole.ordinal())
    			.writeInt(mSelected ? 1 : 0);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mConsole).append(' ').append(mSelected ? "selected" : "deselected");
	}
}