package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * A packet sent by the game master console to the server which causes a message
 * to be displayed on a client.
 * @author rjwut
 */
public class GameMasterMessagePacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.GM_TEXT;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameMasterMessagePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameMasterMessagePacket(reader);
			}
		});
	}

    private final Console mConsole;
    private final CharSequence mSender;
    private final CharSequence mMessage;

    private GameMasterMessagePacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
        byte console = reader.readByte();
        mConsole = console != 0 ? Console.values()[console - 1] : null;
        mSender = reader.readString();
        mMessage = reader.readString();
    }

    /**
     * A message from the game master that will be received as a normal COMMs
     * message. The sender can be any arbitrary String; it doesn't have to
     * match the name of an existing object. This is a convenience constructor
     * for GameMasterMessage(String, String, null).
     */
    public GameMasterMessagePacket(String sender, String message) {
    	this(sender, message, null);
    }

    /**
     * A message from the game master that will be received by one of the
     * consoles. The sender can by any arbitrary String; it doesn't have to
     * match the name of an existing argument. If the console argument is null,
     * it will be received as a normal COMMs message. Otherwise, it will be
     * displayed as a popup on the named Console. Only the six main console
     * types (MAIN_SCREEN, HELM, WEAPONS, ENGINEERING, SCIENCE, COMMUNICATIONS)
     * are allowed.
     */
    public GameMasterMessagePacket(String sender, String message, Console console) {
        super(ConnectionType.CLIENT, TYPE);

        if (sender == null || sender.length() == 0) {
        	throw new IllegalArgumentException("You must provide a sender");
        }

        if (message == null || message.length() == 0) {
        	throw new IllegalArgumentException("You must provide a message");
        }

        if (console != null && console.ordinal() > Console.COMMUNICATIONS.ordinal()) {
        	throw new IllegalArgumentException("Invalid console: " + console);
        }

        mSender = sender;
        mMessage = message;
        mConsole = console;
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

    /**
     * The Console that should receive display a popup containing the message,
     * or null if the message should be sent as a normal COMMs message.  Only
     * the six main console types (MAIN_SCREEN, HELM, WEAPONS, ENGINEERING,
     * SCIENCE, COMMUNICATIONS) are allowed.
     */
    public Console getConsole() {
    	return mConsole;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
		writer
			.writeByte((byte) (mConsole == null ? 0 : mConsole.ordinal() + 1))
			.writeString(mSender)
			.writeString(mMessage);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		if (mConsole == null) {
			b.append(" [COMMs message] ");
		} else {
			b.append(" [").append(mConsole).append(" popup] ");
		}

		b.append(mSender).append(": ").append(mMessage);
	}
}