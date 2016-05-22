package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ShipActionPacket;
import com.walkertribe.ian.world.Artemis;

/**
 * Unloads the indicated tube.
 */
public class UnloadTubePacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_UNLOAD_TUBE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return UnloadTubePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new UnloadTubePacket(reader);
			}
		});
	}

	/**
	 * @param tube Index of the tube to unload, [0 - Artemis.MAX_TUBES)
	 */
    public UnloadTubePacket(int tube) {
        super(TYPE_UNLOAD_TUBE, tube);

        if (tube < 0 || tube >= Artemis.MAX_TUBES) {
        	throw new IndexOutOfBoundsException(
        			"Invalid tube index: " + tube
        	);
        }
    }

    private UnloadTubePacket(PacketReader reader) {
    	super(TYPE_UNLOAD_TUBE, reader);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}