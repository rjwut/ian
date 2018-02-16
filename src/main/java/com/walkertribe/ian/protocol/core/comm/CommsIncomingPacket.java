package com.walkertribe.ian.protocol.core.comm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.Util;

/**
 * Received when an incoming COMMs message arrives.
 */
public class CommsIncomingPacket extends BaseArtemisPacket {
	public static final int MIN_PRIORITY_VALUE = 0;
	public static final int MAX_PRIORITY_VALUE = 8;

	private static final PacketType TYPE = CorePacketType.COMM_TEXT;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return CommsIncomingPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new CommsIncomingPacket(reader);
			}
		});
	}

    private final int mPriority;
    private final CharSequence mFrom;
    private final CharSequence mMessage;

    private CommsIncomingPacket(PacketReader reader) {
        super(ConnectionType.SERVER, TYPE);
        mPriority = reader.readInt();
        mFrom = reader.readString();
        mMessage = Util.caratToNewline(reader.readString());
    }

    public CommsIncomingPacket(int priority, CharSequence from, CharSequence message) {
    	super(ConnectionType.SERVER, TYPE);

    	if (priority < MIN_PRIORITY_VALUE || priority > MAX_PRIORITY_VALUE) {
    		throw new IllegalArgumentException("Invalid priority: " + priority);
    	}

    	if (from == null || from.length() == 0) {
    		throw new IllegalArgumentException("You must provide a sender name");
    	}

    	if (message == null || message.length() == 0) {
    		throw new IllegalArgumentException("You must provide a message");
    	}

    	mPriority = priority;
    	mFrom = from;
    	mMessage = message;
    }

    /**
     * Returns the message priority, with lower values having higher priority.
     * @return An integer between 0 and 8, inclusive
     */
    public int getPriority() {
        return mPriority;
    }

    /**
     * A String identifying the sender. This may not correspond to the name of
     * a game entity. For example, some messages from bases or friendly ships
     * have additional detail after the entity's name ("DS3 TSN Base"). Messages
     * in scripted scenarios can have any String for the sender.
     */
    public CharSequence getFrom() {
        return mFrom;
    }

    /**
     * The content of the message.
     */
    public CharSequence getMessage() {
        return mMessage;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mPriority);
		writer.writeString(mFrom);
		writer.writeString(Util.newlineToCarat(mMessage));
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("from ").append(mFrom).append(": ").append(getMessage());
	}
}