package com.walkertribe.ian.protocol.core.world;

import java.util.Arrays;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * ObjectParser implementation for engineering console updates
 * @author rjwut
 */
public class EngParser extends AbstractObjectParser {
	private enum Bit {
		HEAT_BEAMS,
		HEAT_TORPEDOES,
		HEAT_SENSORS,
		HEAT_MANEUVERING,
		HEAT_IMPULSE,
		HEAT_WARP_OR_JUMP,
		HEAT_FORE_SHIELDS,
		HEAT_AFT_SHEILDS,

		ENERGY_BEAMS,
		ENERGY_TORPEDOES,
		ENERGY_SENSORS,
		ENERGY_MANEUVERING,
		ENERGY_IMPULSE,
		ENERGY_WARP_OR_JUMP,
		ENERGY_FORE_SHIELDS,
		ENERGY_AFT_SHIELDS,

		COOLANT_BEAMS,
		COOLANT_TORPEDOES,
		COOLANT_SENSORS,
		COOLANT_MANEUVERING,
		COOLANT_IMPULSE,
		COOLANT_WARP_OR_JUMP,
		COOLANT_FORE_SHIELDS,
		COOLANT_AFT_SHIELDS;
	}
	private static final Bit[] BITS = Bit.values();
	private static final Bit[] HEAT;
	private static final Bit[] ENERGY;
	private static final Bit[] COOLANT;

	static {
		HEAT = Arrays.copyOfRange(BITS, Bit.HEAT_BEAMS.ordinal(), Bit.ENERGY_BEAMS.ordinal());
		ENERGY = Arrays.copyOfRange(BITS, Bit.ENERGY_BEAMS.ordinal(), Bit.COOLANT_BEAMS.ordinal());
		COOLANT = Arrays.copyOfRange(BITS, Bit.COOLANT_BEAMS.ordinal(), BITS.length);
	}

	EngParser() {
		super(ObjectType.ENGINEERING_CONSOLE);
	}

	@Override
	public int getBitCount() {
		return BITS.length;
	}

	@Override
	protected ArtemisPlayer parseImpl(PacketReader reader) {
        ArtemisPlayer player = new ArtemisPlayer(reader.getObjectId(), ObjectType.ENGINEERING_CONSOLE);

        for (ShipSystem sys : ShipSystem.values()) {
        	if (reader.has(HEAT[sys.ordinal()])) {
            	player.setSystemHeat(sys, reader.readFloat());
        	}
        }

        for (ShipSystem sys : ShipSystem.values()) {
        	if (reader.has(ENERGY[sys.ordinal()])) {
            	player.setSystemEnergy(sys, reader.readFloat());
        	}
        }

        for (ShipSystem sys : ShipSystem.values()) {
        	if (reader.has(COOLANT[sys.ordinal()])) {
            	player.setSystemCoolant(sys, reader.readByte());
        	}
        }

        return player;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisPlayer player = (ArtemisPlayer) obj;

		for (ShipSystem sys : ShipSystem.values()) {
			writer.writeFloat(HEAT[sys.ordinal()], player.getSystemHeat(sys));
		}

		for (ShipSystem sys : ShipSystem.values()) {
			writer.writeFloat(ENERGY[sys.ordinal()], player.getSystemEnergy(sys));
		}

		for (ShipSystem sys : ShipSystem.values()) {
			writer.writeByte(COOLANT[sys.ordinal()], (byte) player.getSystemCoolant(sys), (byte) -1);
		}
	}
}
