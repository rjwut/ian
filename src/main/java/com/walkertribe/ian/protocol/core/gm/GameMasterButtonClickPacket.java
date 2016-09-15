package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ShipActionPacket;
import com.walkertribe.ian.util.JamCrc;

/**
 * Sent by the client whenever the game master clicks a custom on-screen button.
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

	/**
	 * Creates a click command packet for the button with the given label.
	 */
	public GameMasterButtonClickPacket(String label) {
		super(TYPE_GM_BUTTON_CLICK);
		mArg = JamCrc.compute(label);
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
