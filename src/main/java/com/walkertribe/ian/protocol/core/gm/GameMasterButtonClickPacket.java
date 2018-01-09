package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.util.JamCrc;

/**
 * Sent by the client whenever the game master clicks a custom on-screen button.
 * @author rjwut
 */
public class GameMasterButtonClickPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.GM_BUTTON_CLICK, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameMasterButtonClickPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameMasterButtonClickPacket(reader);
			}
		});
	}

	private int mUnknown = 0x0d;

	/**
	 * Creates a click command packet for the button with the given label.
	 */
	public GameMasterButtonClickPacket(String label) {
		super(SubType.GM_BUTTON_CLICK, JamCrc.compute(label));
	}

	/**
	 * Creates a click command packet for the button with the given label hash.
	 */
	public GameMasterButtonClickPacket(int hash) {
		super(SubType.GM_BUTTON_CLICK, hash);
	}

	private GameMasterButtonClickPacket(PacketReader reader) {
		// Note we don't call super(SubType, PacketReader) because we read
		// another value between the subtype and the arg.
		super(SubType.GM_BUTTON_CLICK);
		reader.skip(4); // subtype
		mUnknown = reader.readInt();
		mArg = reader.readInt();
	}

	/**
	 * Returns the label hash for the button that was clicked.
	 */
	public int getHash() {
		return mArg;
	}

    @Override
	protected void writePayload(PacketWriter writer) {
    	// Note that we don't call the superclass implementation here. This is
    	// because we insert a value between the subtype and arg.
		writer.writeInt(SubType.GM_BUTTON_CLICK.ordinal());
		writer.writeInt(mUnknown);
		writer.writeInt(mArg);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("hash=").append(mArg);
	}
}
