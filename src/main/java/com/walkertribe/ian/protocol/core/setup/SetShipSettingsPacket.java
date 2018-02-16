package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.enums.VesselAttribute;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * Set the name, type and drive of ship your console has selected.
 * @author dhleong
 */
public class SetShipSettingsPacket extends BaseArtemisPacket {
	public static void register(PacketFactoryRegistry registry) {
    	registry.register(
    			ConnectionType.CLIENT,
    			CorePacketType.VALUE_INT,
    			SubType.SHIP_SETUP,
    			new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SetShipSettingsPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SetShipSettingsPacket(reader);
			}
		});
	}

	private Ship mShip;

	/**
	 * Use this constructor if you wish to use a Vessel instance from the
	 * VesselData class.
	 */
	public SetShipSettingsPacket(DriveType drive, Vessel vessel, float color, String name) {
        super(ConnectionType.CLIENT, CorePacketType.VALUE_INT);

        if (vessel == null) {
        	throw new IllegalArgumentException("You must specify a Vessel");
        }

        if (!vessel.is(VesselAttribute.PLAYER)) {
        	throw new IllegalArgumentException("Must select a player vessel");
        }

		mShip = new Ship(name, vessel.getId(), color, drive);
	}

	/**
	 * Use this constructor if you wish to use a hull ID.
	 */
	public SetShipSettingsPacket(DriveType drive, int hullId, float color, String name) {
        super(ConnectionType.CLIENT, CorePacketType.VALUE_INT);
		mShip = new Ship(name, hullId, color, drive);
    }

	private SetShipSettingsPacket(PacketReader reader) {
        super(ConnectionType.CLIENT, CorePacketType.VALUE_INT);
        reader.skip(4); // subtype
		DriveType drive = DriveType.values()[reader.readInt()];
		int hullId = reader.readInt();
		float color = reader.readFloat();
		CharSequence name = reader.readString();
		mShip = new Ship(name, hullId, color, drive);
	}

	/**
	 * Returns the Ship object that contains the data described by this packet.
	 */
	public Ship getShip() {
		return mShip;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer	.writeInt(SubType.SHIP_SETUP.ordinal())
				.writeInt(mShip.getDrive().ordinal())
				.writeInt(mShip.getShipType())
				.writeFloat(mShip.getAccentColor())
				.writeString(mShip.getName());
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
    	b.append(mShip);
	}
}