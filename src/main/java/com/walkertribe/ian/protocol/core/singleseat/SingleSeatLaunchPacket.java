package com.walkertribe.ian.protocol.core.singleseat;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;

/**
 * Requests that a particular single-seat craft be launched.
 * @author rjwut
 */
public class SingleSeatLaunchPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SINGLE_SEAT_LAUNCH, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SingleSeatLaunchPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SingleSeatLaunchPacket(reader);
			}
		});
	}

	/**
	 * Requests the launch of the single-seat craft with the given ID.
	 */
    public SingleSeatLaunchPacket(int objectId) {
        super(SubType.SINGLE_SEAT_LAUNCH, objectId);
    }

    private SingleSeatLaunchPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * The object ID of the single-seat craft to launch.
     */
    public int getObjectId() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}
