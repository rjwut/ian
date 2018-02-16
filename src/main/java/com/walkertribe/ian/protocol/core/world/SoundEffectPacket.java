package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;

/**
 * Indicates that the client should play the indicated sound file.
 * @author rjwut
 */
public class SoundEffectPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SOUND_EFFECT, new PacketFactory() {
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

	private CharSequence mFilename;

	private SoundEffectPacket(PacketReader reader) {
		super(SubType.SOUND_EFFECT, reader);
		mFilename = reader.readString();
	}

	public SoundEffectPacket(CharSequence filename) {
		super(SubType.SOUND_EFFECT);
		mFilename = filename;
	}

	/**
	 * Returns the path of the file to play, relative to the Artemis install
	 * directory.
	 */
	public CharSequence getFilename() {
		return mFilename;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeString(mFilename);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mFilename);
	}
}