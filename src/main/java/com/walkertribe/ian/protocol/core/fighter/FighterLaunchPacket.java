package com.walkertribe.ian.protocol.core.fighter;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ShipActionPacket;

/**
 * Requests that a particular fighter be launched.
 * @author rjwut
 */
public class FighterLaunchPacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_LAUNCH_PACKET,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return FighterLaunchPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new FighterLaunchPacket(reader);
			}
		});
	}

	/**
	 * Requests the launch of the fighter with the given ID.
	 */
    public FighterLaunchPacket(int objectId) {
        super(TYPE_LAUNCH_PACKET, objectId);
    }

    private FighterLaunchPacket(PacketReader reader) {
    	super(TYPE_LAUNCH_PACKET, reader);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}
