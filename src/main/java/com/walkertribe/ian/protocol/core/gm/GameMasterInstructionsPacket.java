package com.walkertribe.ian.protocol.core.gm;

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
 * Instructions for the game master, displayed when the game master clicks the
 * "Instructions" button at the upper-left of the stock client.
 * @author rjwut
 */
public class GameMasterInstructionsPacket extends BaseArtemisPacket {
	private static final PacketType TYPE = CorePacketType.GM_TEXT;
	private static final byte SUBTYPE = 0x63;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameMasterInstructionsPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameMasterInstructionsPacket(reader);
			}
		});
	}

	private CharSequence mTitle;
	private CharSequence mContent;

	public GameMasterInstructionsPacket(String title, String content) {
		super(ConnectionType.SERVER, TYPE);
		mTitle = title;
		mContent = content;
	}

	private GameMasterInstructionsPacket(PacketReader reader) {
		super(ConnectionType.SERVER, TYPE);
        reader.skip(1); // subtype
        mTitle = reader.readString();
        mContent = reader.readString();
	}

	/**
	 * The title to display above the instructions.
	 */
	public CharSequence getTitle() {
		return mTitle;
	}

	/**
	 * The actual body text of the instructions.
	 */
	public CharSequence getContent() {
		return mContent;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer
			.writeByte(SUBTYPE)
			.writeString(mTitle)
			.writeString(mContent);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mTitle).append('\n').append(mContent);
	}
}
