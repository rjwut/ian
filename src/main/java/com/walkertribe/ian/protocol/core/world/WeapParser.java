package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.OrdnanceType;
import com.walkertribe.ian.enums.TubeState;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.Artemis;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * ObjectParser implementation for weapons console updates
 * @author rjwut
 */
public class WeapParser extends AbstractObjectParser {
	private enum Bit {
		TORP_HOMING,
		TORP_NUKE,
		TORP_MINE,
		TORP_EMP,
		TORP_PSHOCK,
		TORP_BEACON,
		TORP_PROBE,
		TORP_TAG,

		TUBE_TIME_1,
		TUBE_TIME_2,
		TUBE_TIME_3,
		TUBE_TIME_4,
		TUBE_TIME_5,
		TUBE_TIME_6,
		TUBE_STATE_1,
		TUBE_STATE_2,

		TUBE_STATE_3,
		TUBE_STATE_4,
		TUBE_STATE_5,
		TUBE_STATE_6,
		TUBE_CONTENT_1,
		TUBE_CONTENT_2,
		TUBE_CONTENT_3,
		TUBE_CONTENT_4,

		TUBE_CONTENT_5,
		TUBE_CONTENT_6
	}
	private static final int BIT_COUNT = Bit.values().length;

	private static final Bit[] TORPEDOS = {
        Bit.TORP_HOMING, Bit.TORP_NUKE, Bit.TORP_MINE, Bit.TORP_EMP,
        Bit.TORP_PSHOCK, Bit.TORP_BEACON, Bit.TORP_PROBE, Bit.TORP_TAG
    };

    private static final Bit[] TUBE_TIMES = {
        Bit.TUBE_TIME_1, Bit.TUBE_TIME_2, Bit.TUBE_TIME_3,
        Bit.TUBE_TIME_4, Bit.TUBE_TIME_5, Bit.TUBE_TIME_6
    };

    private static final Bit[] TUBE_STATES = {
        Bit.TUBE_STATE_1, Bit.TUBE_STATE_2, Bit.TUBE_STATE_3,
        Bit.TUBE_STATE_4, Bit.TUBE_STATE_5, Bit.TUBE_STATE_6
    };

    private static final Bit[] TUBE_CONTENTS = {
        Bit.TUBE_CONTENT_1, Bit.TUBE_CONTENT_2, Bit.TUBE_CONTENT_3,
        Bit.TUBE_CONTENT_4, Bit.TUBE_CONTENT_5, Bit.TUBE_CONTENT_6
    };


    protected WeapParser() {
		super(ObjectType.WEAPONS_CONSOLE);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisPlayer parseImpl(PacketReader reader) {
        int[] torps = new int[TORPEDOS.length];
        boolean[] hasTubeTime = new boolean[Artemis.MAX_TUBES];
        float[] tubeTimes = new float[Artemis.MAX_TUBES];
        TubeState[] tubeStates = new TubeState[Artemis.MAX_TUBES];
        byte[] tubeContents = new byte[Artemis.MAX_TUBES];

        for (int i = 0; i < torps.length; i++) {
            torps[i] = (reader.readByte(TORPEDOS[i], (byte) -1));
        }
           
        for (int i = 0; i < Artemis.MAX_TUBES; i++) {
        	Bit bit = TUBE_TIMES[i];

        	if (reader.has(bit)) {
                tubeTimes[i] = reader.readFloat(TUBE_TIMES[i]);
                hasTubeTime[i] = true;
        	}
        }

        for (int i = 0; i < Artemis.MAX_TUBES; i++) {
        	byte state = reader.readByte(TUBE_STATES[i], (byte) -1);

        	if (state != -1) {
        		tubeStates[i] = TubeState.values()[state];
        	}
        }

        for (int i = 0; i < Artemis.MAX_TUBES; i++) {
        	tubeContents[i] = reader.readByte(TUBE_CONTENTS[i], (byte) -1);
        }

        ArtemisPlayer player = new ArtemisPlayer(reader.getObjectId(), ObjectType.WEAPONS_CONSOLE);

        for (int i = 0; i < TORPEDOS.length; i++) {
        	player.setTorpedoCount(OrdnanceType.values()[i], torps[i]);
        }

        for (int i = 0; i < Artemis.MAX_TUBES; i++) {
        	if (hasTubeTime[i]) {
            	player.setTubeCountdown(i, tubeTimes[i]);
        	}

        	player.setTubeState(i, tubeStates[i]);
        	player.setTubeContentsValue(i, tubeContents[i]);
        }

        return player;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisPlayer player = (ArtemisPlayer) obj;
		OrdnanceType[] ordTypes = OrdnanceType.values();

		for (int i = 0; i < TORPEDOS.length; i++) {
			OrdnanceType type = ordTypes[i];
			writer.writeByte(TORPEDOS[i], (byte) player.getTorpedoCount(type), (byte) -1);
		}

        for (int i = 0; i < Artemis.MAX_TUBES; i++) {
        	if (player.isTubeHasCountdown(i)) {
        		writer.writeObjectFloat(TUBE_TIMES[i], player.getTubeCountdown(i));
        	}
        }

        for (int i = 0; i < Artemis.MAX_TUBES; i++) {
        	TubeState state = player.getTubeState(i);
        	byte stateByte = (byte) (state != null ? state.ordinal() : -1);
        	writer.writeByte(TUBE_STATES[i], stateByte, (byte) -1);
        }

        for (int i = 0; i < Artemis.MAX_TUBES; i++) {
        	byte type = player.getTubeContentsValue(i);
        	writer.writeByte(TUBE_CONTENTS[i], type, (byte) -1);
        }
	}
}
