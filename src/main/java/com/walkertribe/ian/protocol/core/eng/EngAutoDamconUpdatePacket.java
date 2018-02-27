package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Notifies the client of the state of autonomous DAMCON.
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.AUTO_DAMCON)
public class EngAutoDamconUpdatePacket extends SimpleEventPacket {
    private final boolean mOn;

    public EngAutoDamconUpdatePacket(boolean on) {
        mOn = on;
    }

    public EngAutoDamconUpdatePacket(PacketReader reader) {
        super(reader);
        mOn = reader.readInt() == 1;
    }

    /**
     * Returns true if autonomous DAMCON was turned on; false if turned off.
     */
    public boolean isOn() {
    	return mOn;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	super.writePayload(writer);
		writer.writeInt(mOn ? 1 : 0);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mOn ? "ON" : "OFF");
	}
}