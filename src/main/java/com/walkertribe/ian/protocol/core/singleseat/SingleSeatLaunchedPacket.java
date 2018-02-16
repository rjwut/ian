package com.walkertribe.ian.protocol.core.singleseat;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;

/**
 * Notifies the client that a single-seat craft has been launched.
 * @author rjwut
 */
public class SingleSeatLaunchedPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SINGLE_SEAT_LAUNCHED, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SingleSeatLaunchedPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SingleSeatLaunchedPacket(reader);
			}
		});
	}

    private final int mObjectId;

    private SingleSeatLaunchedPacket(PacketReader reader) {
        super(SubType.SINGLE_SEAT_LAUNCHED, reader);
        mObjectId = reader.readInt();
    }

    public SingleSeatLaunchedPacket(int objectId) {
        super(SubType.SINGLE_SEAT_LAUNCHED);
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
