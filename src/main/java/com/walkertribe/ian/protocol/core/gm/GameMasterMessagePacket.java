package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.Origin;

import java.util.LinkedHashSet;
import java.util.Set;

import com.walkertribe.ian.enums.CommFilter;
import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.Util;

/**
 * A packet sent by the game master console to the server which causes a message
 * to be displayed on a client.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.GM_TEXT)
public class GameMasterMessagePacket extends BaseArtemisPacket {
	public enum Presentation {
		COMM_MESSAGE,
		PRE_GAME_CHAT,
		POPUP;

		private static Presentation fromInt(int value) {
			switch (value) {
			case 0:
				return COMM_MESSAGE;
			case 3000:
				return PRE_GAME_CHAT;
			default:
				return POPUP;
			}
		}
	}

	private Set<Integer> mRecipients = new LinkedHashSet<Integer>();
	private Presentation mPresentation;
    private Console mConsole;
	private CommFilter mFilter;
    private CharSequence mSender;
    private CharSequence mMessage;

    /**
     * Create a new GM message. If presentation is POPUP, you must provide a console, and is
     * ignored otherwise. The filter is ignored if the presentation is PRE_GAME_CHAT; if it is
     * null, "general" is assumed. 
     */
    public GameMasterMessagePacket(Set<Integer> recipients, Presentation presentation, Console console,
    		CommFilter filter, CharSequence sender, CharSequence message) {
    	if (recipients == null || recipients.isEmpty()) {
    		throw new IllegalArgumentException("You must provide recipients");
    	}

    	if (presentation == null) {
    		throw new IllegalArgumentException("You must provide a presentation");
    	}

    	if (presentation == Presentation.POPUP && console == null) {
    		throw new IllegalArgumentException("Popup messages must specify a console");
    	}

    	if (Util.isBlank(mSender)) {
    		throw new IllegalArgumentException("You must provide a sender");
    	}

    	if (Util.isBlank(mMessage)) {
    		throw new IllegalArgumentException("You must provide a message");
    	}

    	mRecipients = recipients;
    	mPresentation = presentation;
    	mConsole = presentation == Presentation.POPUP ? console : null;
    	mFilter = presentation == Presentation.PRE_GAME_CHAT ? null : filter;
    	mSender = sender;
    	mMessage = message;
    }

    public GameMasterMessagePacket(PacketReader reader) {
    	int id;

    	do {
    		id = reader.readInt();

    		if (id != 0) {
    			mRecipients.add(id);
    		}
    	} while (id != 0);

    	int presentation = reader.readInt();
    	mPresentation = Presentation.fromInt(presentation);

    	if (mPresentation == Presentation.POPUP) {
    		mConsole = Console.values()[presentation - 1];
    	}

        mSender = reader.readString();
        mMessage = reader.readString();

        if (mPresentation != Presentation.PRE_GAME_CHAT) {
        	Set<CommFilter> filterSet = CommFilter.fromInt(reader.readInt());

        	if (!filterSet.isEmpty()) {
        		mFilter = filterSet.iterator().next();
        	}
        }
    }

    /**
     * Returns a Set containing the IDs of the ships that are to receive this message.
     */
    public Set<Integer> getRecipients() {
    	return mRecipients;
    }

    /**
     * Returns the Presentation for this message.
     */
    public Presentation getPresentation() {
    	return mPresentation;
    }

    /**
     * If this message has a POPUP presentation, this returns the Console that should display the
     * popup, or null if the message is not a popup.
     */
    public Console getConsole() {
    	return mConsole;
    }

    /**
     * The CommFilter for this message, if its presentation is not PRE_GAME_CHAT, or null if it is
     * a pre-game chat message. If this method returns null for a non-pre-game chat message, it is
     * a "general" message which matches none of the filters.
     */
    public CommFilter getFilter() {
    	return mFilter;
    }

    /**
     * The message's sender. This can be any arbitrary String and does not have
     * to match the name of an existing object.
     */
    public CharSequence getSender() {
        return mSender;
    }

    /**
     * The content of the message being sent.
     */
    public CharSequence getMessage() {
    	return mMessage;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	for (Integer recipient : mRecipients) {
    		writer.writeInt(recipient);
    	}

    	writer.writeInt(0);

    	if (mPresentation == Presentation.COMM_MESSAGE) {
    		writer.writeInt(0);
    	} else if (mPresentation == Presentation.PRE_GAME_CHAT) {
    		writer.writeInt(3000);
    	} else {
    		writer.writeInt(mConsole.ordinal() + 1);
    	}

    	writer
			.writeString(mSender)
			.writeString(mMessage);

    	if (mPresentation != Presentation.PRE_GAME_CHAT) {
    		writer.writeInt(mFilter != null ? mFilter.toInt() : 0);
    	}
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		switch (mPresentation) {
		case COMM_MESSAGE:
			b.append(" [COMMs message]");
			break;
		case PRE_GAME_CHAT:
			b.append(" [pre-game chat]");
			break;
		default:
			b.append(" [").append(mConsole).append(" popup]");
		}

		b.append(" To:");

		for (Integer recipient : getRecipients()) {
			b.append(" #").append(recipient);
		}

		if (mPresentation != Presentation.PRE_GAME_CHAT) {
			b.append(" filter=" + (mFilter != null ? mFilter : "GENERAL"));
		}

		b.append("; From: ").append(mSender).append(": ").append(mMessage);
	}
}