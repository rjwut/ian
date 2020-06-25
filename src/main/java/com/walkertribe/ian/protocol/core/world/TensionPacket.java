package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Sets the current level of tension in the simulation, which affects the music played by the main
 * screen. The music is grouped into three tiers: low, medium, and high tension. Whenever the client
 * finishes playing a track, it will look at the current tension level and select a new track that
 * corresponds to that level of tension. However, if the client receives a TensionPacket that
 * indicates a higher level of tension than the current track, it will immediately fade out the
 * current track and start playing a new one for the new level of tension.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.TENSION)
public class TensionPacket extends SimpleEventPacket {
    private float mTension;

    /**
     * Creates a new TensionPacket for the indicated level of tension.
     */
    public TensionPacket(float tension) {
        mTension = tension;
    }

    /**
     * Reads a TensionPacket from the given PacketReader.
     */
    public TensionPacket(PacketReader reader) {
        super(reader);
        mTension = reader.readFloat();
    }

    /**
     * Returns the tension level, as a float between 0.0 and 100.0.
     */
    public float getTension() {
        return mTension;
    }

    @Override
    protected void writePayload(PacketWriter writer) {
        super.writePayload(writer);
        writer.writeFloat(mTension);
    }

    @Override
    protected void appendPacketDetail(StringBuilder b) {
        b.append("tension=").append(mTension);
    }
}
