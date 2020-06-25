package com.walkertribe.ian.protocol.core.comm;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.CommsMessage;
import com.walkertribe.ian.enums.CommsRecipientType;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * Sends a message to another entity.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.COMMS_MESSAGE)
public class CommsOutgoingPacket extends BaseArtemisPacket {
    public static final int NO_ARG = 0x00730078;
    private static final int NO_ARG_2 = 0x004f005e;

    private CommsRecipientType mRecipientType;
    private int mRecipientId;
    private CommsMessage mMsg;
    private int mArg;
    private int mArg2 = NO_ARG_2;

    /**
     * Creates an outgoing message with no argument. If you pass in a
     * CommMessage which requires an argument, an IllegalArgumentException will
     * be thrown.
     */
    public CommsOutgoingPacket(ArtemisPlayer sender, ArtemisObject recipient, CommsMessage msg,
    		Context ctx) {
        this(sender, recipient, msg, NO_ARG, ctx);
    }
    
    /**
     * Creates an outgoing message with an argument. At this writing, only the
     * {@link com.walkertribe.ian.enums.OtherMessage#GO_DEFEND}
     * message takes an argument, which is the ID of the object to be defended.
     * For messages with no argument, you can pass in {@link #NO_ARG}, but it's
     * easier to just use the other constructor. An IllegalArgumentException
     * will be thrown if you provide an argument to a message which doesn't
     * accept one, or use NO_ARG with a message which requires one.
     */
    public CommsOutgoingPacket(ArtemisPlayer sender, ArtemisObject recipient, CommsMessage msg,
            int arg, Context ctx) {
        if (sender == null) {
            throw new IllegalArgumentException("You must provide a sender");
        }

        if (recipient == null) {
        	throw new IllegalArgumentException("You must provide a recipient");
        }

        if (msg == null) {
        	throw new IllegalArgumentException("You must provide a message");
        }

        mRecipientType = CommsRecipientType.fromObject(sender, recipient, ctx);

    	if (mRecipientType == null) {
    		throw new IllegalArgumentException("Recipient cannot receive messages");
    	}

    	CommsRecipientType messageRecipientType = msg.getRecipientType();

    	if (mRecipientType != messageRecipientType) {
    		throw new IllegalArgumentException(
    				"Recipient type is " + mRecipientType +
    				", but message recipient type is " + messageRecipientType
    		);
    	}

        if (msg.hasArgument() && arg == NO_ARG) {
        	throw new IllegalArgumentException(
        			"Message " + msg + " requires an argument"
        	);
        }

        if (!msg.hasArgument() && arg != NO_ARG) {
        	throw new IllegalArgumentException(
        			"Message " + msg + " does not accept an argument"
        	);
        }

    	mRecipientId = recipient.getId();
    	mMsg = msg;
    	mArg = arg;
    }

    public CommsOutgoingPacket(PacketReader reader) {
        mRecipientType = CommsRecipientType.values()[reader.readInt()];
        mRecipientId = reader.readInt();
        mMsg = mRecipientType.messageFromId(reader.readInt());
        mArg = reader.readInt();
        mArg2 = reader.readInt();
    }

    /**
     * The CommsRecipientType value corresponding to the target object.
     */
    public CommsRecipientType getRecipientType() {
    	return mRecipientType;
    }

    /**
     * The ID of the target object.
     */
    public int getRecipientId() {
    	return mRecipientId;
    }

    /**
     * The enum value representing the message to send.
     */
    public CommsMessage getMessage() {
    	return mMsg;
    }

    /**
     * The argument to send with the message.
     */
	public int getArgument() {
		return mArg;
	}

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer	.writeInt(mRecipientType.ordinal())
    			.writeInt(mRecipientId)
    			.writeInt(mMsg.getId())
    			.writeInt(mArg)
    			.writeInt(mArg2);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("to obj #").append(mRecipientId).append(": ").append(mMsg);

		if (mArg != NO_ARG) {
			b.append(" object #").append(mArg);
		}
	}
}