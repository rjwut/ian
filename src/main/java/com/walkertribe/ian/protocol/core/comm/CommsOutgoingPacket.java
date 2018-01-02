package com.walkertribe.ian.protocol.core.comm;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.CommsMessage;
import com.walkertribe.ian.enums.CommsRecipientType;
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
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Sends a message to another entity.
 */
public class CommsOutgoingPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.COMMS_MESSAGE;
    public static final int NO_ARG = 0x00730078;
    private static final int NO_ARG_2 = 0x004f005e;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return CommsOutgoingPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new CommsOutgoingPacket(reader);
			}
		});
	}

    private CommsRecipientType mRecipientType;
    private int mRecipientId;
    private CommsMessage mMsg;
    private int mArg;

    /**
     * Creates an outgoing message with no argument. If you pass in a
     * CommMessage which requires an argument, an IllegalArgumentException will
     * be thrown.
     */
    public CommsOutgoingPacket(ArtemisObject target, CommsMessage msg,
    		Context ctx) {
        this(target, msg, NO_ARG, ctx);
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
    public CommsOutgoingPacket(ArtemisObject recipient, CommsMessage msg,
            int arg, Context ctx) {
        super(ConnectionType.CLIENT, TYPE);

        if (recipient == null) {
        	throw new IllegalArgumentException("You must provide a recipient");
        }

        if (msg == null) {
        	throw new IllegalArgumentException("You must provide a message");
        }

        mRecipientType = CommsRecipientType.fromObject(recipient, ctx);

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

    private CommsOutgoingPacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
        mRecipientType = CommsRecipientType.values()[reader.readInt()];
        mRecipientId = reader.readInt();
        mMsg = mRecipientType.messageFromId(reader.readInt());
        mArg = reader.readInt();
        reader.skip(4);	// arg 2 placeholder
    }

    public CommsRecipientType getRecipientType() {
    	return mRecipientType;
    }

    public int getRecipientId() {
    	return mRecipientId;
    }

    public CommsMessage getMessage() {
    	return mMsg;
    }

	public int getArgument() {
		return mArg;
	}

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer	.writeInt(mRecipientType.ordinal())
    			.writeInt(mRecipientId)
    			.writeInt(mMsg.getId())
    			.writeInt(mArg)
    			.writeInt(NO_ARG_2);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("to obj #").append(mRecipientId).append(": ").append(mMsg);

		if (mArg != NO_ARG) {
			b.append(" object #").append(mArg);
		}
	}
}