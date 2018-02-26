package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.GameType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;

/**
 * Sent by the server when the simulation starts.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.START_GAME)
public class GameStartPacket extends BaseArtemisPacket {
	public static final int MIN_DIFFICULTY = 1;
	public static final int MAX_DIFFICULTY = 11;

    private int difficulty;
    private GameType gameType;

    public GameStartPacket(int difficulty, GameType gameType) {
    	if (difficulty < MIN_DIFFICULTY || difficulty > MAX_DIFFICULTY) {
    		throw new IllegalArgumentException(
    				"Invalid difficulty level (" +
    				difficulty +
    				"); must be between " +
    				MIN_DIFFICULTY +
    				" and " + 
    				MAX_DIFFICULTY
    		);
    	}

    	if (gameType == null) {
    		throw new IllegalArgumentException("You must provide a game type");
    	}

    	this.difficulty = difficulty;
    	this.gameType = gameType;
	}

    public GameStartPacket(PacketReader reader) {
		difficulty = reader.readInt();
		gameType = GameType.values()[reader.readInt()];
    }

    /**
     * The simulation's difficulty level, a value between 1 and 11, inclusive.
     */
    public int getDifficulty() {
    	return difficulty;
    }

    /**
     * What type of simulation is running (siege, single front, etc.)
     */
    public GameType getGameType() {
    	return gameType;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer.writeInt(difficulty).writeInt(gameType.ordinal());
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b	.append("difficulty = ")
			.append(difficulty)
			.append(", game type = ")
			.append(gameType);
	}
}