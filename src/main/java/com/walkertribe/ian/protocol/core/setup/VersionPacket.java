package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.Version;

/**
 * Gives the Artemis server's version number. Sent immediately after
 * WelcomePacket.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.CONNECTED)
public class VersionPacket extends BaseArtemisPacket {
	private int mUnknown;
	private Version mVersion;

	public VersionPacket(Version version) {
		mVersion = version;
	}

	public VersionPacket(PacketReader reader) {
		mUnknown = reader.readInt();
		float fVersion = reader.readFloat();

		if (reader.hasMore()) {
			mVersion = new Version(
					reader.readInt(),
					reader.readInt(),
					reader.readInt()
			);
		} else {
			mVersion = new Version(fVersion);
		}
	}

	/**
	 * @return The version number
	 */
	public Version getVersion() {
		return mVersion;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mUnknown);
		mVersion.writeTo(writer);
		writer.setVersion(mVersion);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mVersion);
	}
}