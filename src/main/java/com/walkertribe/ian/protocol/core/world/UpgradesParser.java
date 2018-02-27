package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.Upgrade;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * ObjectParser implementation for player ship upgrade updates
 * @author rjwut
 */
public class UpgradesParser extends AbstractObjectParser {
	/**
	 * Represents the three fields about an upgrade in a player ship.
	 */
	private enum Field {
		ACTIVE {
			protected void read(PacketReader reader, ArtemisPlayer player, Upgrade upgrade) {
				player.setUpgradeActive(upgrade, reader.readBool(getBitIndex(upgrade), 1));
			}

			@Override
			protected void write(PacketWriter writer, ArtemisPlayer player, Upgrade upgrade) {
				writer.writeBool(getBitIndex(upgrade), player.isUpgradeActive(upgrade), 1);
			}
		},
		COUNT {
			protected void read(PacketReader reader, ArtemisPlayer player, Upgrade upgrade) {
				player.setUpgradeCount(upgrade, reader.readByte(getBitIndex(upgrade), (byte) -1));
			}

			@Override
			protected void write(PacketWriter writer, ArtemisPlayer player, Upgrade upgrade) {
				writer.writeByte(getBitIndex(upgrade), player.getUpgradeCount(upgrade), (byte) -1);
			}
		},
		TIME_REMAINING {
			protected void read(PacketReader reader, ArtemisPlayer player, Upgrade upgrade) {
				player.setUpgradeSecondsLeft(upgrade, reader.readShort(getBitIndex(upgrade), -1));
			}

			@Override
			protected void write(PacketWriter writer, ArtemisPlayer player, Upgrade upgrade) {
				writer.writeShort(getBitIndex(upgrade), player.getUpgradeSecondsLeft(upgrade), -1);
			}
		};

		/**
		 * Reads the field for this Upgrade from the PacketReader and store it
		 * in the ArtemisPlayer object.
		 */
		protected abstract void read(PacketReader reader, ArtemisPlayer player, Upgrade upgrade);

		/**
		 * Writes the value stored in the ArtemisPlayer object for the field
		 * for this Upgrade to the PacketWriter.
		 */
		protected abstract void write(PacketWriter writer, ArtemisPlayer player, Upgrade upgrade);

		/**
		 * Returns the bit index for the given Field/Upgrade combination.
		 */
		protected int getBitIndex(Upgrade upgrade) {
			return ordinal() * Upgrade.ACTIVATION_UPGRADE_COUNT + upgrade.getActivationIndex();
		}
	}
	private static final int BIT_COUNT = Field.values().length * Upgrade.ACTIVATION_UPGRADE_COUNT;

	UpgradesParser() {
		super(ObjectType.UPGRADES);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisPlayer parseImpl(PacketReader reader) {
		ArtemisPlayer player = new ArtemisPlayer(reader.getObjectId(), ObjectType.UPGRADES);

		for (Field field : Field.values()) {
			for (Upgrade upgrade : Upgrade.activation()) {
				field.read(reader, player, upgrade);
			}
		}

		return player;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisPlayer player = (ArtemisPlayer) obj;

		for (Field field : Field.values()) {
			for (Upgrade upgrade : Upgrade.activation()) {
				field.write(writer, player, upgrade);
			}
		}
	}
}