package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.GameType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;

/**
 * Sent by the server when the simulation starts.
 * @author rjwut
 */
public class GameStartPacket extends BaseArtemisPacket {
	private static final PacketType TYPE = CorePacketType.START_GAME;
	public static final int MIN_DIFFICULTY = 1;
	public static final int MAX_DIFFICULTY = 11;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameStartPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameStartPacket(reader);
			}
		});
	}

    private int difficulty;
    private GameType gameType;

    private GameStartPacket(PacketReader reader) {
		super(ConnectionType.SERVER, TYPE);
		difficulty = reader.readInt();
		gameType = GameType.values()[reader.readInt()];
    }

    public GameStartPacket(int difficulty, GameType gameType) {
		super(ConnectionType.SERVER, TYPE);

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