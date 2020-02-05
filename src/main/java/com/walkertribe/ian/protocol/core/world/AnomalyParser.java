package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.AnomalyType;
import com.walkertribe.ian.enums.BeaconMode;
import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.ObjectType;
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
		TYPE,
		SCAN,
		UNK_1_6,
		BEACON_TYPE,
		BEACON_MODE
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
        anomaly.setX(reader.readFloat(Bit.X));
        anomaly.setY(reader.readFloat(Bit.Y));
        anomaly.setZ(reader.readFloat(Bit.Z));

        if (reader.has(Bit.TYPE)) {
        	anomaly.setAnomalyType(AnomalyType.values()[reader.readInt()]);
        }

        if (reader.has(Bit.SCAN)) {
            anomaly.setScanBits(reader.readInt(Bit.SCAN, 0));
        }

        reader.readObjectUnknown(Bit.UNK_1_6, 4);

        if (reader.has(Bit.BEACON_TYPE)) {
        	anomaly.setBeaconType(CreatureType.values()[reader.readByte()]);
        }

        if (reader.has(Bit.BEACON_MODE)) {
        	anomaly.setBeaconMode(BeaconMode.values()[reader.readByte()]);
        }

        return anomaly;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisAnomaly anomaly = (ArtemisAnomaly) obj;
		writer.writeFloat(Bit.X, anomaly.getX());
		writer.writeFloat(Bit.Y, anomaly.getY());
		writer.writeFloat(Bit.Z, anomaly.getZ());

		AnomalyType anomalyType = anomaly.getAnomalyType();

		if (anomalyType != null) {
			writer.writeInt(Bit.TYPE, anomalyType.ordinal(), -1);
		}

		Integer scan = anomaly.getScanBits();

		if (scan != null) {
			writer.writeInt(Bit.SCAN, scan, 1);
		}

		writer.writeUnknown(Bit.UNK_1_6);

		CreatureType beaconType = anomaly.getBeaconType();

		if (beaconType != null) {
			writer.writeByte(Bit.BEACON_TYPE, (byte) beaconType.ordinal(), (byte) -1);
		}

		BeaconMode beaconMode = anomaly.getBeaconMode();

		if (beaconMode != null) {
			writer.writeByte(Bit.BEACON_MODE, (byte) beaconMode.ordinal(), (byte) -1);
		}
	}
}