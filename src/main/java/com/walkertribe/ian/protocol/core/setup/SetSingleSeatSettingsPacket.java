package com.walkertribe.ian.protocol.core.setup;

import java.util.ArrayList;
import java.util.List;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Sent by the client to update the single-seat craft settings.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.SINGLE_SEAT_SETTINGS)
public class SetSingleSeatSettingsPacket extends BaseArtemisPacket {
	private static final byte[] UNKNOWN = new byte[] { 0, 0, 0, 0 };

	private byte[] mUnknown;
	private List<SingleSeatCraft> mCraftList;

	public SetSingleSeatSettingsPacket(List<SingleSeatCraft> craftList) {
		mUnknown = UNKNOWN;
		mCraftList = craftList;
	}

	public SetSingleSeatSettingsPacket(PacketReader reader) {
		reader.skip(4); // subtype
		mUnknown = reader.readBytes(4);
		mCraftList = new ArrayList<SingleSeatCraft>();

		do {
			SingleSeatCraft craft = SingleSeatCraft.read(reader);

			if (craft == null) {
				break;
			}

			mCraftList.add(craft);
		} while (true);
	}

	/**
	 * Returns a List of the SingleSeatCraft contained in this packet.
	 */
	public List<SingleSeatCraft> getCraftList() {
		return mCraftList;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(SubType.SINGLE_SEAT_SETTINGS);
		writer.writeBytes(mUnknown);

		for (SingleSeatCraft craft : mCraftList) {
			craft.write(writer);
		}

		writer.writeInt(0);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		for (SingleSeatCraft craft : mCraftList) {
			b.append('\n').append(craft);
		}
	}
}
