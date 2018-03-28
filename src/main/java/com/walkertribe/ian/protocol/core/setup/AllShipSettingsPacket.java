package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;
import com.walkertribe.ian.world.Artemis;

/**
 * Sent by the server to update the names, types and drives for each ship.
 * @author dhleong
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.SHIP_SETTINGS)
public class AllShipSettingsPacket extends SimpleEventPacket {
	private final Ship[] mShips;

    public AllShipSettingsPacket(Ship[] ships) {
        super(SubType.SHIP_SETTINGS);

        if (ships == null) {
        	throw new IllegalArgumentException("Ship array can't be null");
        }

        if (ships.length != Artemis.SHIP_COUNT) {
        	throw new IllegalArgumentException(
        			"Must specify " + Artemis.SHIP_COUNT + " ships"
        	);
        }

        for (Ship ship : ships) {
        	if (ship == null) {
            	throw new IllegalArgumentException(
            			"Must specify " + Artemis.SHIP_COUNT + " ships"
            	);
        	}
        }

        mShips = ships;
    }

    public AllShipSettingsPacket(PacketReader reader) {
        super(reader);
        mShips = new Ship[Artemis.SHIP_COUNT];

        for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
        	mShips[i] = Ship.read(reader);
        }
    }

    /**
     * Returns the ship with the given index (0-based).
     */
    public Ship getShip(int shipIndex) {
    	return mShips[shipIndex];
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);

		for (Ship ship : mShips) {
			ship.write(writer);
		}
	}

    @Override
	protected void appendPacketDetail(StringBuilder b) {
        for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
        	b.append("\n\t").append(mShips[i]);
        }
	}
}