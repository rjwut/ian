package com.walkertribe.ian.protocol.core.singleseat;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Notifies the client that a single-seat craft has been launched.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.SINGLE_SEAT_LAUNCHED)
public class SingleSeatLaunchedPacket extends SimpleEventPacket {
    private final int mObjectId;

    public SingleSeatLaunchedPacket(int objectId) {
    	mObjectId = objectId;
    }

    public SingleSeatLaunchedPacket(PacketReader reader) {
        super(reader);
        mObjectId = reader.readInt();
    }

    /**
     * The craft's ID.
     */
    public int getObjectId() {
        return mObjectId;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeInt(mObjectId);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mObjectId);
	}
}
