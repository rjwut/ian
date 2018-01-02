package com.walkertribe.ian.protocol.core.world;

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

/**
 * Indicates that the client should play the indicated sound file.
 * @author rjwut
 */
public class SoundEffectPacket extends BaseArtemisPacket {
	private static final PacketType TYPE = CorePacketType.SIMPLE_EVENT;
	private static final byte MSG_TYPE = 0x03;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, MSG_TYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SoundEffectPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SoundEffectPacket(reader);
			}
		});
	}

	private String mFilename;

	private SoundEffectPacket(PacketReader reader) {
		super(ConnectionType.SERVER, TYPE);
        reader.skip(4); // subtype
		mFilename = reader.readString();
	}

	/**
	 * Returns the path of the file to play, relative to the Artemis install
	 * directory.
	 */
	public String getFilename() {
		return mFilename;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(MSG_TYPE).writeString(mFilename);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mFilename);
	}
}