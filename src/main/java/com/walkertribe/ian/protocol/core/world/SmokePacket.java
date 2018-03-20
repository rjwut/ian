package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Sent by the server when the client should render smoke being emitted.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.SMOKE)
public class SmokePacket extends SimpleEventPacket {
	private int mObjectId;
	private int mUnknown;
	private float mX;
	private float mY;
	private float mZ;

	public SmokePacket(int objectId, float x, float y, float z) {
		super();
		mObjectId = objectId;
		mX = x;
		mY = y;
		mZ = z;
	}

	public SmokePacket(PacketReader reader) {
		super(reader);
		mObjectId = reader.readInt();
		mUnknown = reader.readInt(); // Ship system index? Smoke type?
		mX = reader.readFloat();
		mY = reader.readFloat();
		mZ = reader.readFloat();
	}

	/**
	 * The ID of the object emitting the smoke.
	 */
	public int getObjectId() {
		return mObjectId;
	}

	/**
	 * The X-coordinate relative to the ship's origin where the smoke is emitted. Some extreme values have been
	 * observed; maybe smoke left after a ship is destroyed?
	 */
	public float getX() {
		return mX;
	}

	/**
	 * The Y-coordinate relative to the ship's origin where the smoke is emitted. Some extreme values have been
	 * observed; maybe smoke left after a ship is destroyed?
	 */
	public float getY() {
		return mY;
	}

	/**
	 * The Z-coordinate relative to the ship's origin where the smoke is emitted. Some extreme values have been
	 * observed; maybe smoke left after a ship is destroyed?
	 */
	public float getZ() {
		return mZ;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer
			.writeInt(mObjectId)
			.writeInt(mUnknown)
			.writeFloat(mX)
			.writeFloat(mY)
			.writeFloat(mZ);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b	.append('#')
			.append(mObjectId)
			.append(" (")
			.append(mX)
			.append(',')
			.append(mY)
			.append(',')
			.append(mZ)
			.append(')');
	}

}
