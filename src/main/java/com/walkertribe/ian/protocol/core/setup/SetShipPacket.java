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
    public static final int SHIP_1_ARTEMIS  = 0;
    public static final int SHIP_2_INTREPID = 1;
    public static final int SHIP_3_AEGIS    = 2;
    public static final int SHIP_4_HORATIO  = 3;
    public static final int SHIP_5_EXCALIBUR= 4;
    public static final int SHIP_6_HERA     = 5;
    public static final int SHIP_7_CERES	= 6;
    public static final int SHIP_8_DIANA	= 7;
    
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
     * @param shipIndex Index [0,7] of the ship you want to be on. The SHIP_*
     * constants are provided for reference, but the names can, of course, be
     * changed.
     */
    public SetShipPacket(int shipIndex) {
        super(TYPE_SET_SHIP, shipIndex);
    }

    private SetShipPacket(PacketReader reader) {
        super(TYPE_SET_SHIP, reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}