package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Updates the client about the rage level of the biomech tribe.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.BIOMECH_STANCE)
public class BiomechRagePacket extends SimpleEventPacket {
    private int mRage;

    /**
     * Creates a new BiomechRagePacket that sets the biomech rage level to the given value.
     */
    public BiomechRagePacket(int rage) {
        mRage = rage;
    }

    /**
     * Reads a BiomechRagePacket from the given PacketReader.
     */
    public BiomechRagePacket(PacketReader reader) {
        super(reader);
        mRage = reader.readInt();
    }

    /**
     * Returns the biomech rage level.
     */
    public int getRage() {
        return mRage;
    }

    @Override
    protected void writePayload(PacketWriter writer) {
        super.writePayload(writer);
        writer.writeInt(mRage);
    }

    @Override
    protected void appendPacketDetail(StringBuilder b) {
        b.append("rage=").append(mRage);
    }
}
