package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.world.ArtemisPlayer;

public class DockedPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.DOCKED, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return DockedPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new DockedPacket(reader);
			}
		});
	}

    private final int mObjectId;

    private DockedPacket(PacketReader reader) {
        super(SubType.DOCKED, reader);
        mObjectId = reader.readInt();
    }

    public DockedPacket(ArtemisPlayer player) {
    	this(player.getId());
    }

    public DockedPacket(int objectId) {
        super(SubType.DOCKED);
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
