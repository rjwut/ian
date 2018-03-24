package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.Origin;

import java.util.HashSet;
import java.util.Set;

import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.Util;
import com.walkertribe.ian.world.Artemis;

/**
 * A packet sent by the game master console to the server which causes a message
 * to be displayed on a client.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.GM_TEXT)
public class GameMasterMessagePacket extends BaseArtemisPacket {
	private BoolState[] mRecipients = new BoolState[Artemis.SHIP_COUNT];
    private Console mConsole;
    private CharSequence mSender;
    private CharSequence mMessage;

    public GameMasterMessagePacket() {
    	for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
    		mRecipients[i] = BoolState.FALSE;
    	}
    }

    public GameMasterMessagePacket(PacketReader reader) {
    	for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
    		mRecipients[i] = reader.readBool(1);
    	}

    	int console = reader.readInt();
        mConsole = console != 0 ? Console.values()[console - 1] : null;
        mSender = reader.readString();
        mMessage = reader.readString();
    }

    /**
     * Returns a Set containing the indices of the ships that are to receive this message.
     */
    public Set<Integer> getRecipients() {
    	Set<Integer> set = new HashSet<Integer>();

    	for (int i = 0; i < mRecipients.length; i++) {
    		BoolState recipient = mRecipients[i];

    		if (recipient.getBooleanValue()) {
    			set.add(i);
    		}
    	}

    	return set;
    }

    /**
     * Sets whether the ship with the given index is a recipient or not.
     */
    public void setRecipient(int index, boolean isRecipient) {
    	mRecipients[index] = BoolState.from(isRecipient);
    }

    /**
     * The message's sender. This can be any arbitrary String and does not have
     * to match the name of an existing object.
     */
    public CharSequence getSender() {
        return mSender;
    }

    public void setSender(CharSequence sender) {
        if (Util.isBlank(sender)) {
        	throw new IllegalArgumentException("You must provide a sender");
        }

    	mSender = sender;
    }

    /**
     * The content of the message being sent.
     */
    public CharSequence getMessage() {
    	return mMessage;
    }

    public void setMessage(CharSequence message) {
        if (Util.isBlank(message)) {
        	throw new IllegalArgumentException("You must provide a message");
        }

    	mMessage = message;
    }

    /**
     * The Console that should receive display a popup containing the message,
     * or null if the message should be sent as a normal COMMs message.  Only
     * the six main console types (MAIN_SCREEN, HELM, WEAPONS, ENGINEERING,
     * SCIENCE, COMMUNICATIONS) are allowed.
     */
    public Console getConsole() {
    	return mConsole;
    }

    public void setConsole(Console console) {
        if (console != null && console.ordinal() > Console.COMMUNICATIONS.ordinal()) {
        	throw new IllegalArgumentException("Invalid console: " + console);
        }

        mConsole = console;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	for (BoolState recipient : mRecipients) {
    		writer.writeBool(recipient, 1);
    	}

    	writer
			.writeInt(mConsole == null ? 0 : mConsole.ordinal() + 1)
			.writeString(mSender)
			.writeString(mMessage);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		if (mConsole == null) {
			b.append(" [COMMs message]");
		} else {
			b.append(" [").append(mConsole).append(" popup]");
		}

		b.append(" To:");

		for (Integer recipient : getRecipients()) {
			b.append(' ').append(recipient);
		}

		b.append("; From: ").append(mSender).append(": ").append(mMessage);
	}
}