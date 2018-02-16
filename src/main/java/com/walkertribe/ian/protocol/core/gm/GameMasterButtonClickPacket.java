package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.util.JamCrc;
import com.walkertribe.ian.util.TextUtil;

/**
 * Sent by the client whenever the game master clicks a custom on-screen button.
 * @author rjwut
 */
public class GameMasterButtonClickPacket extends BaseArtemisPacket {
	public static void register(PacketFactoryRegistry registry) {
    	registry.register(
    			ConnectionType.CLIENT,
    			CorePacketType.VALUE_INT,
    			(byte) SubType.GM_BUTTON_CLICK.ordinal(),
    			new PacketFactory() {
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
	private int mHash;

	/**
	 * Creates a click command packet for the button with the given label.
	 */
	public GameMasterButtonClickPacket(CharSequence label) {
		this(JamCrc.compute(label));
	}

	/**
	 * Creates a click command packet for the button with the given label hash.
	 */
	public GameMasterButtonClickPacket(int hash) {
        super(ConnectionType.CLIENT, CorePacketType.VALUE_INT);
        mHash = hash;
	}

	private GameMasterButtonClickPacket(PacketReader reader) {
        super(ConnectionType.CLIENT, CorePacketType.VALUE_INT);
		reader.skip(4); // subtype
		mUnknown = reader.readInt();
		mHash = reader.readInt();
	}

	/**
	 * Returns the label hash for the button that was clicked.
	 */
	public int getHash() {
		return mHash;
	}

    @Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(SubType.GM_BUTTON_CLICK.ordinal());
		writer.writeInt(mUnknown);
		writer.writeInt(mHash);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("hash=").append(TextUtil.intToHexLE(mHash));
	}
}
