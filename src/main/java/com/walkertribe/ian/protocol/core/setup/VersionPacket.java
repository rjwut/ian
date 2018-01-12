package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.Version;

/**
 * Gives the Artemis server's version number. Sent immediately after
 * WelcomePacket.
 * @author rjwut
 */
public class VersionPacket extends BaseArtemisPacket {
	public static final PacketType TYPE = CorePacketType.CONNECTED;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return VersionPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new VersionPacket(reader);
			}
		});
	}

	private int mUnknown;
	private Version mVersion;

	private VersionPacket(PacketReader reader) {
		super(ConnectionType.SERVER, TYPE);
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

	public VersionPacket(Version version) {
		super(ConnectionType.SERVER, TYPE);
		mVersion = version;
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