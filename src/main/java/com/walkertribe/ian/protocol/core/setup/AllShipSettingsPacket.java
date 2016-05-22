package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.UnexpectedTypeException;
import com.walkertribe.ian.vesseldata.Vessel;
import com.walkertribe.ian.vesseldata.VesselData;
import com.walkertribe.ian.world.Artemis;

/**
 * Sent by the server to update the names, types and drives for each ship.
 * @author dhleong
 */
public class AllShipSettingsPacket extends BaseArtemisPacket {
    private static final int TYPE = 0xf754c8fe;
    private static final byte MSG_TYPE = 0x0f;
    
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, MSG_TYPE, new PacketFactory() {
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

	public static class Ship {
		private String mName;
		private int mShipType;
		private DriveType mDrive;

		public Ship(String name, int shipType, DriveType drive) {
			mName = name;
			mShipType = shipType;
			mDrive = drive;
		}

		public String getName() {
			return mName;
		}

		public int getShipType() {
			return mShipType;
		}

		public DriveType getDrive() {
			return mDrive;
		}

		@Override
		public String toString() {
        	Vessel vessel = VesselData.get().getVessel(mShipType);
			StringBuilder b = new StringBuilder();
        	b	.append(mName)
    			.append(" (")
    			.append(vessel != null ? vessel.getName() : "UNKNOWN TYPE")
    			.append(") [")
    			.append(mDrive)
    			.append(']');
        	return b.toString();
		}
	}

	private final Ship[] mShips;

    private AllShipSettingsPacket(PacketReader reader) {
        super(ConnectionType.SERVER, TYPE);
        mShips = new Ship[Artemis.SHIP_COUNT];
        int subtype = reader.readInt();

        if (subtype != MSG_TYPE) {
        	throw new UnexpectedTypeException(subtype, MSG_TYPE);
        }
        
        for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
        	DriveType drive = DriveType.values()[reader.readInt()];
        	int shipType = reader.readInt();
        	reader.skip(4); // RJW: UNKNOWN INT (always seems to be 1 0 0 0)
        	String name = reader.readString();
        	mShips[i] = new Ship(name, shipType, drive);
        }
    }

    public AllShipSettingsPacket(Ship[] ships) {
        super(ConnectionType.SERVER, TYPE);

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

        	if (ship.getDrive() == null) {
            	throw new IllegalArgumentException("Must specify ship drive");
        	}

        	String name = ship.getName();

        	if (name == null || name.trim().length() == 0) {
            	throw new IllegalArgumentException("Must specify ship name");
        	}
        }

        mShips = ships;
    }

    public Ship getShip(int shipIndex) {
    	return mShips[shipIndex];
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(MSG_TYPE);

		for (Ship ship : mShips) {
			writer.writeInt(ship.mDrive.ordinal());
			writer.writeInt(ship.mShipType);
			writer.writeInt(1); // RJW: UNKNOWN INT (always seems to be 1 0 0 0)
			writer.writeString(ship.mName);
		}
	}

    @Override
	protected void appendPacketDetail(StringBuilder b) {
        for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
        	b.append("\n\t").append(mShips[i]);
        }
	}
}