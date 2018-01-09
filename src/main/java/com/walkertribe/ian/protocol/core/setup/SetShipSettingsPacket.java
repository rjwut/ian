package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.enums.VesselAttribute;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.vesseldata.Vessel;

/**
 * Set the name, type and drive of ship your console has selected.
 * @author dhleong
 */
public class SetShipSettingsPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SHIP_SETUP, new PacketFactory() {
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

	private DriveType mDrive;
	private int mHullId;
	private String mName;

	/**
	 * Use this constructor if you wish to use a Vessel instance from the
	 * VesselData class.
	 */
	public SetShipSettingsPacket(DriveType drive, Vessel vessel, String name) {
        super(SubType.SHIP_SETUP);

        if (vessel == null) {
        	throw new IllegalArgumentException("You must specify a Vessel");
        }

        if (!vessel.is(VesselAttribute.PLAYER)) {
        	throw new IllegalArgumentException("Must select a player vessel");
        }

        init(drive, vessel.getId(), name);
	}

	/**
	 * Use this constructor if you wish to use a hull ID.
	 */
	public SetShipSettingsPacket(DriveType drive, int hullId, String name) {
        super(SubType.SHIP_SETUP);
        init(drive, hullId, name);
    }

	private SetShipSettingsPacket(PacketReader reader) {
        super(SubType.SHIP_SETUP);
        reader.skip(4); // subtype
		mDrive = DriveType.values()[reader.readInt()];
		mHullId = reader.readInt();
		reader.skip(4); // RJW: UNKNOWN INT (always seems to be 1 0 0 0)
		mName = reader.readString();
	}

	private void init(DriveType drive, int hullId, String name) {
        if (drive == null) {
        	throw new IllegalArgumentException("You must specify a drive type");
        }

        if (name == null) {
        	throw new IllegalArgumentException("You must specify a name");
        }

        mDrive = drive;
        mHullId = hullId;
        mName = name;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer	.writeInt(SubType.SHIP_SETUP.ordinal())
				.writeInt(mDrive.ordinal())
				.writeInt(mHullId)
				.writeInt(1) // RJW: UNKNOWN INT (always seems to be 1 0 0 0)
				.writeString(mName);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
    	b	.append(mName).append(": hull ID #")
    		.append(mHullId)
    		.append(" [").append(mDrive).append(']');
	}
}