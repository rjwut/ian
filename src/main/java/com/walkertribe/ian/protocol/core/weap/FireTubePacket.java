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
 * Fire whatever's in the given tube.
 * @author dhleong
 */
public class FireTubePacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_FIRE_TUBE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return FireTubePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new FireTubePacket(reader);
			}
		});
	}

	/**
	 * @param tube The index of the tube to fire, [0 - Artemis.MAX_TUBES)
	 */
    public FireTubePacket(int tube) {
        super(TYPE_FIRE_TUBE, tube);

        if (tube < 0 || tube >= Artemis.MAX_TUBES) {
        	throw new IndexOutOfBoundsException(
        			"Invalid tube index: " + tube
        	);
        }
    }

    private FireTubePacket(PacketReader reader) {
    	super(TYPE_FIRE_TUBE, reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}