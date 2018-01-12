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
	private float mColor;
	private String mName;

	/**
	 * Use this constructor if you wish to use a Vessel instance from the
	 * VesselData class.
	 */
	public SetShipSettingsPacket(DriveType drive, Vessel vessel, float color, String name) {
        super(SubType.SHIP_SETUP);

        if (vessel == null) {
        	throw new IllegalArgumentException("You must specify a Vessel");
        }

        if (!vessel.is(VesselAttribute.PLAYER)) {
        	throw new IllegalArgumentException("Must select a player vessel");
        }

        init(drive, vessel.getId(), color, name);
	}

	/**
	 * Use this constructor if you wish to use a hull ID.
	 */
	public SetShipSettingsPacket(DriveType drive, int hullId, float color, String name) {
        super(SubType.SHIP_SETUP);
        init(drive, hullId, color, name);
    }

	private SetShipSettingsPacket(PacketReader reader) {
        super(SubType.SHIP_SETUP);
        reader.skip(4); // subtype
		mDrive = DriveType.values()[reader.readInt()];
		mHullId = reader.readInt();
		mColor = reader.readFloat();
		mName = reader.readString();
	}

	private void init(DriveType drive, int hullId, float color, String name) {
        if (drive == null) {
        	throw new IllegalArgumentException("You must specify a drive type");
        }

        if (color < 0.0f || color >= 1.0f) {
        	throw new IllegalArgumentException("Color must be in range [0.0,1.0)");
        }

        if (name == null || name.length() == 0) {
        	throw new IllegalArgumentException("You must specify a name");
        }

        mDrive = drive;
        mHullId = hullId;
        mColor = color;
        mName = name;
	}

	/**
	 * The ship's drive type
	 */
	public DriveType getDrive() {
		return mDrive;
	}

	/**
	 * The ship's vessel ID
	 */
	public int getHullId() {
		return mHullId;
	}

	/**
	 * A float in range [0.0,1.0) describing the accent color. Multiply this
	 * value by 360 to get a hue expressed in degrees.
	 * @see https://en.wikipedia.org/wiki/Hue
	 */
	public float getAccentColor() {
		return mColor;
	}

	/**
	 * The ship's name
	 */
	public String getName() {
		return mName;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer	.writeInt(SubType.SHIP_SETUP.ordinal())
				.writeInt(mDrive.ordinal())
				.writeInt(mHullId)
				.writeFloat(mColor)
				.writeString(mName);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
    	b	.append(mName)
    		.append(": hull ID #").append(mHullId)
    		.append(" [").append(mDrive)
    		.append("], hue=").append(mColor * 360).append(" deg");
	}
}