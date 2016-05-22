package com.walkertribe.ian.protocol.core.sci;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ShipActionPacket;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Scans the indicated target.
 */
public class SciScanPacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_SCI_SCAN,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SciScanPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SciScanPacket(reader);
			}
		});
	}

	/**
	 * @param target The target to scan
	 */
    public SciScanPacket(ArtemisObject target) {
        super(TYPE_SCI_SCAN, target != null ? target.getId() : 0);

        if (target == null) {
        	throw new IllegalArgumentException("You must provide a target");
        }
    }

    private SciScanPacket(PacketReader reader) {
        super(TYPE_SCI_SCAN, reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mArg);
	}
}