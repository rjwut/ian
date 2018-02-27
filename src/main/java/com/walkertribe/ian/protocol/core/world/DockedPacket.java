package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * Sent when a player ship docks.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.DOCKED)
public class DockedPacket extends SimpleEventPacket {
    private final int mObjectId;

    public DockedPacket(ArtemisPlayer player) {
    	this(player.getId());
    }

    public DockedPacket(int objectId) {
    	mObjectId = objectId;
    }

    public DockedPacket(PacketReader reader) {
        super(reader);
        mObjectId = reader.readInt();
    }

    /**
     * The ID of the ship that has docked.
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
