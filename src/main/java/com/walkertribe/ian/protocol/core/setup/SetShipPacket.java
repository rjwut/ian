package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ShipActionPacket;

/**
 * Set the ship you want to be on. You must send this packet before
 * SetConsolePacket.
 * @author dhleong
 */
public class SetShipPacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_SET_SHIP,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SetShipPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SetShipPacket(reader);
			}
		});
	}

    /**
     * Selects a ship to use during setup. Note that Artemis ship numbers are
     * one-based.
     */
    public SetShipPacket(int shipNumber) {
    	super(TYPE_SET_SHIP, shipNumber - 1); // underlying packet wants index
    }

    private SetShipPacket(PacketReader reader) {
        super(TYPE_SET_SHIP, reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg + 1);
	}
}