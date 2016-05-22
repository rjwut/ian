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
 * Sets the science officer's current target.
 */
public class SciSelectPacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_SCI_SELECT,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SciSelectPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SciSelectPacket(reader);
			}
		});
	}

	/**
	 * @param target The target to select (or null to clear the taregt)
	 */
    public SciSelectPacket(ArtemisObject target) {
        super(TYPE_SCI_SELECT, target == null ? 1 : target.getId());
    }

    public SciSelectPacket(PacketReader reader) {
    	super(TYPE_SCI_SELECT, reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mArg);
	}
}