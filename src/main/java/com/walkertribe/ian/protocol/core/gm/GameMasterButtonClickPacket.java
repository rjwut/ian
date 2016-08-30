package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ShipActionPacket;

/**
 * Sent by the client whenever the game master clicks a custom on-screen button.
 * The argument is a hash value of the button's label, although at this writing
 * it is unknown how this value is computed.
 * @author rjwut
 */
public class GameMasterButtonClickPacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_GM_BUTTON_CLICK,
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

	public GameMasterButtonClickPacket(int hash) {
		super(TYPE_GM_BUTTON_CLICK);
		mArg = hash;
	}

	private GameMasterButtonClickPacket(PacketReader reader) {
		super(TYPE_GM_BUTTON_CLICK);
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
		writer.writeInt(TYPE_GM_BUTTON_CLICK);
		writer.writeInt(mUnknown);
		writer.writeInt(mArg);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("hash=").append(mArg);
	}
}
