package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.world.Artemis;

/**
 * Sent by the server to update the names, types and drives for each ship.
 * @author dhleong
 */
public class AllShipSettingsPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SHIP_SETTINGS, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return AllShipSettingsPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new AllShipSettingsPacket(reader);
			}
		});
	}

	private final Ship[] mShips;

    private AllShipSettingsPacket(PacketReader reader) {
        super(SubType.SHIP_SETTINGS, reader);
        mShips = new Ship[Artemis.SHIP_COUNT];

        for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
        	DriveType drive = DriveType.values()[reader.readInt()];
        	int shipType = reader.readInt();
        	float accentColor = reader.readFloat();
        	reader.skip(4); // RJW: UNKNOWN INT (always seems to be 1 0 0 0)
        	CharSequence name = reader.readString();
        	mShips[i] = new Ship(name, shipType, accentColor, drive);
        }
    }

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

    public Ship getShip(int shipNumber) {
    	return mShips[shipNumber - 1];
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);

		for (Ship ship : mShips) {
			writer.writeInt(ship.getDrive().ordinal());
			writer.writeInt(ship.getShipType());
			writer.writeFloat(ship.getAccentColor());
			writer.writeInt(1); // RJW: UNKNOWN INT (always seems to be 1 0 0 0)
			writer.writeString(ship.getName());
		}
	}

    @Override
	protected void appendPacketDetail(StringBuilder b) {
        for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
        	b.append("\n\t").append(mShips[i]);
        }
	}
}