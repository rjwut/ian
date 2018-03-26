package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.Upgrade;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisAnomaly;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * ObjectParser implementation for anomalies
 * @author rjwut
 */
public class AnomalyParser extends AbstractObjectParser {
	private enum Bit {
		X,
		Y,
		Z,
		UPGRADE,
		SCAN,
		UNKNOWN_1_6
	}
	private static final int BIT_COUNT = Bit.values().length;

	AnomalyParser() {
		super(ObjectType.ANOMALY);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisAnomaly parseImpl(PacketReader reader) {
        ArtemisAnomaly anomaly = new ArtemisAnomaly(reader.getObjectId());
        anomaly.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        anomaly.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        anomaly.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));

        if (reader.has(Bit.UPGRADE)) {
        	anomaly.setUpgrade(Upgrade.fromAnomalyIndex(reader.readInt()));
        }

        if (reader.has(Bit.SCAN)) {
            anomaly.setScanBits(reader.readInt(Bit.SCAN, 0));
        }

        reader.readObjectUnknown(Bit.UNKNOWN_1_6, 4);
        return anomaly;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisAnomaly anomaly = (ArtemisAnomaly) obj;
		writer.writeFloat(Bit.X, anomaly.getX(), Float.MIN_VALUE);
		writer.writeFloat(Bit.Y, anomaly.getY(), Float.MIN_VALUE);
		writer.writeFloat(Bit.Z, anomaly.getZ(), Float.MIN_VALUE);

		Upgrade upgrade = anomaly.getUpgrade();

		if (upgrade != null) {
			writer.writeInt(Bit.UPGRADE, upgrade.getAnomalyIndex(), -1);
		}

		Integer scan = anomaly.getScanBits();

		if (scan != null) {
			writer.writeInt(Bit.SCAN, scan, 1);
		}

		writer.writeUnknown(Bit.UNKNOWN_1_6);
	}
}