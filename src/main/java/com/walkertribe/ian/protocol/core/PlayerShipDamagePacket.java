package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Notifies the client that the indicated ship has received an impact. This
 * manifests as an interface screw on the client.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.PLAYER_SHIP_DAMAGE)
public class PlayerShipDamagePacket extends SimpleEventPacket {
	private int mShipIndex;
	private float mDuration;

    public PlayerShipDamagePacket(int shipIndex, float duration) {
        mShipIndex = shipIndex;
        mDuration = duration;
    }

	public PlayerShipDamagePacket(PacketReader reader) {
		super(reader);
        mShipIndex = reader.readInt();
        mDuration = reader.readFloat();
    }

	/**
	 * The index of the ship being impacted (0-based).
	 */
	public int getShipIndex() {
		return mShipIndex;
	}

	/**
	 * How long the interface screw should last, in seconds.
	 */
	public float getDuration() {
		return mDuration;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeInt(mShipIndex).writeFloat(mDuration);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("Ship #" + mShipIndex + " (" + mDuration + " s)");
	}
}