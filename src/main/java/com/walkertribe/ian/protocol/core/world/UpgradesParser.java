package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.Upgrade;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisPlayer;

public class UpgradesParser extends AbstractObjectParser {
	private enum Bit {
		UNKNOWN_1_1,
		UNKNOWN_1_2,
		UNKNOWN_1_3,
		UNKNOWN_1_4,
		UNKNOWN_1_5,
		UNKNOWN_1_6,
		UNKNOWN_1_7,
		UNKNOWN_1_8,
		UNKNOWN_2_1,
		UNKNOWN_2_2,
		UNKNOWN_2_3,
		UNKNOWN_2_4,
		UNKNOWN_2_5,
		UNKNOWN_2_6,
		UNKNOWN_2_7,
		UNKNOWN_2_8,
		UNKNOWN_3_1,
		UNKNOWN_3_2,
		UNKNOWN_3_3,
		UNKNOWN_3_4,
		UNKNOWN_3_5,
		UNKNOWN_3_6,
		UNKNOWN_3_7,
		UNKNOWN_3_8,
		UNKNOWN_4_1,
		UNKNOWN_4_2,
		UNKNOWN_4_3,
		UNKNOWN_4_4,
		INFUSION_P_COIL(Upgrade.INFUSION_P_COIL),
		UNKNOWN_4_6,
		TAURON_FOCUSER(Upgrade.TAURON_FOCUSER),
		CARPACTION_COIL(Upgrade.CARPACTION_COIL),
		UNKNOWN_5_1,
		CETROCITE_CRYSTAL(Upgrade.CETROCITE_CRYSTAL),
		LATERAL_ARRAY(Upgrade.LATERAL_ARRAY),
		UNKNOWN_5_4,
		SECRET_CODE_CASE(Upgrade.SECRET_CODE_CASE),
		UNKNOWN_5_6,
		UNKNOWN_5_7,
		UNKNOWN_5_8,
		UNKNOWN_6_1,
		UNKNOWN_6_2,
		UNKNOWN_6_3,
		UNKNOWN_6_4,
		UNKNOWN_6_5,
		UNKNOWN_6_6,
		UNKNOWN_6_7,
		UNKNOWN_6_8,
		UNKNOWN_7_1,
		UNKNOWN_7_2,
		UNKNOWN_7_3,
		UNKNOWN_7_4,
		UNKNOWN_7_5,
		UNKNOWN_7_6,
		UNKNOWN_7_7,
		UNKNOWN_7_8,
		UNKNOWN_8_1,
		UNKNOWN_8_2,
		UNKNOWN_8_3,
		UNKNOWN_8_4,
		UNKNOWN_8_5,
		UNKNOWN_8_6,
		UNKNOWN_8_7,
		UNKNOWN_8_8,
		UNKNOWN_9_1,
		UNKNOWN_9_2,
		UNKNOWN_9_3,
		UNKNOWN_9_4,
		UNKNOWN_9_5,
		UNKNOWN_9_6,
		UNKNOWN_9_7,
		UNKNOWN_9_8,
		UNKNOWN_10_1,
		UNKNOWN_10_2,
		UNKNOWN_10_3,
		UNKNOWN_10_4,
		UNKNOWN_10_5,
		UNKNOWN_10_6,
		UNKNOWN_10_7,
		UNKNOWN_10_8,
		UNKNOWN_11_1,
		UNKNOWN_11_2,
		UNKNOWN_11_3,
		UNKNOWN_11_4;

		private Upgrade upgrade;

		Bit() {
		}

		Bit(Upgrade upgrade) {
			this.upgrade = upgrade;
		}

		private Upgrade getUpgrade() {
			return upgrade;
		}
	}

	private static final Bit[] BITS = Bit.values();

	UpgradesParser() {
		super(ObjectType.UPGRADES);
	}

	@Override
	public Bit[] getBits() {
		return BITS;
	}

	@Override
	protected ArtemisPlayer parseImpl(PacketReader reader) {
		ArtemisPlayer player = new ArtemisPlayer(reader.getObjectId());

		for (Bit bit : BITS) {
			if (reader.has(bit)) {
				Upgrade upgrade = bit.getUpgrade();

				if (upgrade != null) {
					player.setUpgrades(upgrade, reader.readByte());
				} else {
					reader.readObjectUnknown(bit, 1);
				}
			}
		}

		reader.readObjectUnknownUntil("UNKNOWN", ObjectType.UPGRADES.getId());
		return player;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisPlayer player = (ArtemisPlayer) obj;

		for (Bit bit : BITS) {
			Upgrade upgrade = bit.getUpgrade();

			if (upgrade != null) {
				writer.writeByte(bit, player.getUpgrades(upgrade), (byte) -1);
			} else {
				writer.writeUnknown(bit);
			}
		}

		writer.writeUnknown("UNKNOWN", null);
	}
}