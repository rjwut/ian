package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

public class PlayerShipDamagePacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.PLAYER_SHIP_DAMAGE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return PlayerShipDamagePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new PlayerShipDamagePacket(reader);
			}
		});
	}

	private int mShipIndex;
	private float mDuration;

	private PlayerShipDamagePacket(PacketReader reader) throws ArtemisPacketException {
        super(SubType.PLAYER_SHIP_DAMAGE, reader);
        mShipIndex = reader.readInt();
        mDuration = reader.readFloat();
    }

    public PlayerShipDamagePacket(int shipIndex, float duration) {
        super(SubType.PLAYER_SHIP_DAMAGE);
        mShipIndex = shipIndex;
        mDuration = duration;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeInt(mShipIndex).writeFloat(mDuration);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("Ship #" + mShipIndex + " (" + mDuration + " s)");
	}
}