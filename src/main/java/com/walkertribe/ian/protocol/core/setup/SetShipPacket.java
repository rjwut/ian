package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.world.Artemis;

/**
 * Set the ship you want to be on. You must send this packet before
 * SetConsolePacket.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.SET_SHIP)
public class SetShipPacket extends ValueIntPacket {
    /**
     * Selects a ship to use during setup. Note that Artemis ship numbers are
     * one-based.
     */
    public SetShipPacket(int shipNumber) {
    	super(shipNumber - 1); // underlying packet wants index

    	if (shipNumber < 1 || shipNumber > Artemis.SHIP_COUNT) {
    		throw new IllegalArgumentException("Ship number must be between 1 and " + Artemis.SHIP_COUNT);
    	}
    }

    public SetShipPacket(PacketReader reader) {
        super(reader);
    }

    /**
     * The ship number being selected (1-based).
     */
    public int getShipNumber() {
    	return mArg + 1;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg + 1);
	}
}