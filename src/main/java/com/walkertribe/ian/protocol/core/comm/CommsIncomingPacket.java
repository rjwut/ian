package com.walkertribe.ian.protocol.core.comm;

import java.util.Set;

import com.walkertribe.ian.enums.CommFilter;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.Util;

/**
 * Received when an incoming COMMs message arrives.
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.COMM_TEXT)
public class CommsIncomingPacket extends BaseArtemisPacket {

    private final Set<CommFilter> mFilters;
    private final CharSequence mFrom;
    private final CharSequence mMessage;

    public CommsIncomingPacket(PacketReader reader) {
    	mFilters = CommFilter.fromInt(reader.readShort());
        mFrom = reader.readString();
        mMessage = Util.caratToNewline(reader.readString());
    }

    public CommsIncomingPacket(Set<CommFilter> filters, CharSequence from, CharSequence message) {
    	if (filters == null) {
    		throw new IllegalArgumentException("You must provide a filter Set");
    	}

    	if (Util.isBlank(from)) {
    		throw new IllegalArgumentException("You must provide a sender name");
    	}

    	if (Util.isBlank(message)) {
    		throw new IllegalArgumentException("You must provide a message");
    	}

    	mFilters = filters;
    	mFrom = from;
    	mMessage = message;
    }

    /**
     * Returns true if this message matches the given CommFilter.
     */
    public boolean matches(CommFilter filter) {
        return mFilters.contains(filter);
    }

    /**
     * A String identifying the sender. This may not correspond to the name of
     * a game entity. For example, some messages from bases or friendly ships
     * have additional detail after the entity's name ("DS3 TSN Deep Space
     * Base"). Messages in scripted scenarios can have any String for the sender.
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
		writer.writeShort(CommFilter.toInt(mFilters));
		writer.writeString(mFrom);
		writer.writeString(Util.newlineToCarat(mMessage));
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("from ").append(mFrom)
		.append(" [").append(Util.joinSpaceDelimited(mFilters))
		.append("]: ").append(getMessage());
	}
}