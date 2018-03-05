package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.enums.ConsoleStatus;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.Artemis;

/**
 * Indicates which consoles are taken; received in response to a
 * SetConsolePacket or SetShipPacket.
 * @author dhleong
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.CLIENT_CONSOLES)
public class ConsoleStatusPacket extends BaseArtemisPacket {
    private final int shipIndex;
    private final ConsoleStatus[] statuses;

    public ConsoleStatusPacket(int shipIndex, ConsoleStatus[] statuses) {
    	if (shipIndex < 0 || shipIndex >= Artemis.SHIP_COUNT) {
    		throw new IllegalArgumentException(
    				"Ship index must be greater than -1 and less than " + Artemis.SHIP_COUNT
    		);
    	}

    	if (statuses == null) {
    		throw new IllegalArgumentException(
    				"Must provide status array"
    		);
    	}

    	if (statuses.length != Console.values().length) {
    		throw new IllegalArgumentException(
    				"Must provide a status for each bridge console"
    		);
    	}

    	for (ConsoleStatus status : statuses) {
    		if (status == null) {
        		throw new IllegalArgumentException(
        				"Must provide a status for each bridge console"
        		);
    		}
    	}

    	this.shipIndex = shipIndex;
    	this.statuses = statuses;
    }
    
    public ConsoleStatusPacket(PacketReader reader) {
        shipIndex = reader.readInt();
        final Console[] consoleValues = Console.values();
        final ConsoleStatus[] statusValues = ConsoleStatus.values();
        statuses = new ConsoleStatus[consoleValues.length];

        for (Console console : consoleValues) {
        	statuses[console.ordinal()] = statusValues[reader.readByte()];
        }
    }

    /**
     * Returns the ship index whose consoles this packet reports.
     */
    public int getShipIndex() {
    	return shipIndex;
    }

    /**
     * Get the status for a specific Console
     * @param console The desired Console
     * @return ConsoleStatus The status of that console
     */
    public ConsoleStatus get(Console console) {
        if (console == null) {
            throw new IllegalArgumentException("You must specify a console");
        }

        return statuses[console.ordinal()];
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(shipIndex);

		for (ConsoleStatus status : statuses) {
			writer.writeByte((byte) status.ordinal());
		}
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("Ship #").append(shipIndex);

		for (Console console : Console.values()) {
    		ConsoleStatus status = statuses[console.ordinal()];
			b.append("\n\t").append(console).append(": ").append(status);
    	}
	}
}