package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.TargetingMode;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Sent by the server when a beam weapon has been fired.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.ATTACK)
public class BeamFiredPacket extends BaseArtemisPacket {
	private static final byte[] ZERO = { 0, 0, 0, 0 };

	private int mBeamId;
	private int mBeamPortIndex;
	private ObjectType mOriginObjectType;
	private ObjectType mTargetObjectType;
	private int mOriginId;
	private int mTargetId;
	private float mImpactX;
	private float mImpactY;
	private float mImpactZ;
	private TargetingMode mTargetingMode;
	private int mUnknown1;
	private int mUnknown2;
	private byte[] mUnknown3 = ZERO;

	public BeamFiredPacket(int beamId) {
		mBeamId = beamId;
	}

	public BeamFiredPacket(PacketReader reader) {
		mBeamId = reader.readInt();
		mUnknown1 = reader.readInt();
		mUnknown2 = reader.readInt();
		mBeamPortIndex = reader.readInt();
		int id = reader.readInt();
		mOriginObjectType = id != 0 ? ObjectType.fromId(id) : null;
		id = reader.readInt();
		mTargetObjectType = id != 0 ? ObjectType.fromId(id) : null;
		mUnknown3 = reader.readBytes(4);
		mOriginId = reader.readInt();
		mTargetId = reader.readInt();
		mImpactX = reader.readFloat();
		mImpactY = reader.readFloat();
		mImpactZ = reader.readFloat();
		mTargetingMode = TargetingMode.get(reader.readInt() != 0);
	}

	/**
	 * The beam's ID.
	 */
	public int getBeamId() {
		return mBeamId;
	}

	/**
	 * The index of the port from which this beam was fired, which is the same
	 * as the index of the corresponding <code>&lt;beam_port&gt;</code> entry in
	 * the shipData.xml file. (So 0 corresponds to the zeroeth
	 * <code>&lt;beam_port&gt;</code> entry for that ship in shipData.xml.)
	 */
	public int getBeamPortIndex() {
		return mBeamPortIndex;
	}

	public void setBeamPortIndex(int beamPortIndex) {
		mBeamPortIndex = beamPortIndex;
	}

	/**
	 * Convenience method that calls setOriginObjectType() and setOriginId().
	 */
	public void setOrigin(ArtemisObject obj) {
		setOriginObjectType(obj != null ? obj.getType() : null);
		setOriginId(obj != null ? obj.getId() : 0);
	}

	/**
	 * The ObjectType of the vessel that fired the beam.
	 */
	public ObjectType getOriginObjectType() {
		return mOriginObjectType;
	}

	public void setOriginObjectType(ObjectType originObjectType) {
		mOriginObjectType = originObjectType;
		mUnknown1 = originObjectType == ObjectType.PLAYER_SHIP ? 1 : 0;
		mUnknown2 = originObjectType == ObjectType.PLAYER_SHIP ? 1200 : 100; // Is this beam strength?
	}

	/**
	 * Convenience method that calls setTargetObjectType() and setTargetId().
	 */
	public void setTarget(ArtemisObject obj) {
		setTargetObjectType(obj != null ? obj.getType() : null);
		setTargetId(obj != null ? obj.getId() : 0);
	}

	/**
	 * Returns the ObjectType of the vessel that was struck by the beam.
	 */
	public ObjectType getTargetObjectType() {
		return mTargetObjectType;
	}

	public void setTargetObjectType(ObjectType targetObjectType) {
		mTargetObjectType = targetObjectType;
	}

	/**
	 * The ID of the object from which the beam was fired.
	 */
	public int getOriginId() {
		return mOriginId;
	}

	public void setOriginId(int originId) {
		mOriginId = originId;
	}

	/**
	 * The ID of the object being fired upon.
	 */
	public int getTargetId() {
		return mTargetId;
	}

	public void setTargetId(int targetId) {
		mTargetId = targetId;
	}

	/**
	 * The X-coordinate (relative to the center of the target) of the impact
	 * point. This is used to determine the endpoint for the beam. A negative
	 * value means an impact on the target's starboard; a positive value means
	 * an impact on the target's port.
	 */
	public float getImpactX() {
		return mImpactX;
	}

	public void setImpactX(float impactX) {
		mImpactX = impactX;
	}

	/**
	 * The Y-coordinate (relative to the center of the target) of the impact
	 * point. This is used to determine the endpoint for the beam. A negative
	 * value means an impact on the target's ventral (bottom) side; a positive
	 * value means an impact on the target's dorsal (top) side.
	 */
	public float getImpactY() {
		return mImpactY;
	}

	public void setImpactY(float impactY) {
		mImpactY = impactY;
	}

	/**
	 * The Z-coordinate (relative to the center of the target) of the impact
	 * point. This is used to determine the endpoint for the beam. A negative
	 * value means an impact on the target's aft; a positive value means an
	 * impact on the target's fore.
	 */
	public float getImpactZ() {
		return mImpactZ;
	}

	public void setImpactZ(float impactZ) {
		mImpactZ = impactZ;
	}

	/**
	 * Returns the targeting mode used to fire these beams.
	 */
	public TargetingMode getTargetingMode() {
		return mTargetingMode;
	}

	public void setTargetingMode(TargetingMode targetingMode) {
		mTargetingMode = targetingMode;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer
			.writeInt(mBeamId)
			.writeInt(mUnknown1)
			.writeInt(mUnknown2)
			.writeInt(mBeamPortIndex)
			.writeInt(mOriginObjectType != null ? mOriginObjectType.getId() : 0)
			.writeInt(mTargetObjectType != null ? mTargetObjectType.getId() : 0)
			.writeBytes(mUnknown3)
			.writeInt(mOriginId)
			.writeInt(mTargetId)
			.writeFloat(mImpactX)
			.writeFloat(mImpactY)
			.writeFloat(mImpactZ)
			.writeInt(mTargetingMode.ordinal());
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b
			.append("Beam #")
			.append(mBeamId)
			.append(" port ")
			.append(mBeamPortIndex)
			.append(" from ship #")
			.append(mOriginId)
			.append(" to ship #")
			.append(mTargetId)
			.append(" (")
			.append(mTargetingMode)
			.append(')');
	}
}