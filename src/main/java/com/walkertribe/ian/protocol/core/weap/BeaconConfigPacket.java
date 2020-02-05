package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.BeaconMode;
import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.BEACON_CONFIG)
public class BeaconConfigPacket extends BaseArtemisPacket {
	private static final byte[] UNKNOWN = new byte[] { 0x39, 0x30, 0x00, 0x00 }; // 12345 

	private byte[] mUnknown;
	private CreatureType mCreatureType;
	private BeaconMode mBeaconMode;

	/**
	 * Program beacons to attract/repel a particular type of creature.
	 */
	public BeaconConfigPacket(CreatureType creatureType, BeaconMode beaconMode) {
		if (creatureType == null || CreatureType.WRECK.equals(creatureType)) {
			throw new IllegalArgumentException("Invalid creature type: " + creatureType);
		}

		if (beaconMode == null) {
			throw new IllegalArgumentException("You must provide a beacon mode");
		}

		mUnknown = UNKNOWN;
		mCreatureType = creatureType;
		mBeaconMode = beaconMode;
	}

	public BeaconConfigPacket(PacketReader reader) {
		reader.skip(4); // subtype
		mUnknown = reader.readBytes(4);
		mCreatureType = CreatureType.values()[reader.readByte()];
		mBeaconMode = BeaconMode.values()[reader.readByte()];
	}

	/**
	 * The type of creatures to affect.
	 */
	public CreatureType getCreatureType() {
		return mCreatureType;
	}

	/**
	 * Whether to attract or repel the creatures.
	 */
	public BeaconMode getBeaconMode() {
		return mBeaconMode;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(SubType.BEACON_CONFIG);
		writer.writeBytes(mUnknown);
		writer.writeByte((byte) mCreatureType.ordinal());
		writer.writeByte((byte) mBeaconMode.ordinal());
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mBeaconMode).append(' ').append(mCreatureType);
	}
}
