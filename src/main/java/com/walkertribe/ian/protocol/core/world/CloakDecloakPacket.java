package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Sent by the server when a ship has cloaked or decloaked. This results in a
 * green "poof" visual effect in the stock client.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.CLOAK_DECLOAK)
public class CloakDecloakPacket extends SimpleEventPacket {
    private final float mX;
    private final float mY;
    private final float mZ;

    public CloakDecloakPacket(float x, float y, float z) {
        mX = x;
        mY = y;
        mZ = z;
    }

    public CloakDecloakPacket(PacketReader reader) {
        super(reader);
        mX = reader.readFloat();
        mY = reader.readFloat();
        mZ = reader.readFloat();
    }

    /**
     * The X-coordinate where the (un)cloak event occurred.
     */
    public float getX() {
        return mX;
    }

    /**
     * The Y-coordinate where the (un)cloak event occurred.
     */
    public float getY() {
        return mY;
    }

    /**
     * The Z-coordinate where the (un)cloak event occurred.
     */
    public float getZ() {
        return mZ;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeFloat(mX).writeFloat(mY).writeFloat(mZ);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('(').append(mX).append(',').append(mY).append(',').append(mZ).append(')');
	}
}
