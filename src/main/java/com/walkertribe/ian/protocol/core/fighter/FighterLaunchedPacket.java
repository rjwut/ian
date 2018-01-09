package com.walkertribe.ian.protocol.core.fighter;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;

/**
 * Notifies the client that a fighter has been launched.
 * @author rjwut
 */
public class FighterLaunchedPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.FIGHTER_LAUNCHED, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return FighterLaunchedPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new FighterLaunchedPacket(reader);
			}
		});
	}

    private final int mObjectId;

    private FighterLaunchedPacket(PacketReader reader) {
        super(SubType.FIGHTER_LAUNCHED, reader);
        mObjectId = reader.readInt();
    }

    public FighterLaunchedPacket(int objectId) {
        super(SubType.FIGHTER_LAUNCHED);
    	mObjectId = objectId;
    }

    public int getObjectId() {
        return mObjectId;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeInt(mObjectId);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mObjectId);
	}
}
