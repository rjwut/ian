package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;
import com.walkertribe.ian.util.BoolState;

/**
 * Notifies the client that the game has paused or unpaused.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.PAUSE)
public class PausePacket extends SimpleEventPacket {
    private final BoolState mPaused;

    public PausePacket(boolean paused) {
    	mPaused = BoolState.from(paused);
    }
    
    public PausePacket(PacketReader reader) {
        super(reader);
        mPaused = reader.readBool(4);
    }

    public boolean isPaused() {
        return mPaused.getBooleanValue();
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeBool(mPaused, 4);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mPaused.getBooleanValue() ? "PAUSED" : "RUNNING");
	}
}