package com.walkertribe.ian.protocol.core.setup;

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
import com.walkertribe.ian.protocol.core.CorePacketType;

public class DifficultyPacket extends BaseArtemisPacket {
	private static final PacketType TYPE = CorePacketType.START_GAME;
	public static final int MIN = 1;
	public static final int MAX = 11;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return DifficultyPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new DifficultyPacket(reader);
			}
		});
	}

    private int difficulty;
    private GameType gameType;

    private DifficultyPacket(PacketReader reader) {
		super(ConnectionType.SERVER, TYPE);
		setDifficulty(reader.readInt());
		setGameType(GameType.values()[reader.readInt()]);
    }

    public DifficultyPacket(int difficulty, GameType gameType) {
		super(ConnectionType.SERVER, TYPE);
		setDifficulty(difficulty);
		setGameType(gameType);
	}

    public int getDifficulty() {
    	return difficulty;
    }

    public void setDifficulty(int difficulty) {
    	if (difficulty < MIN || difficulty > MAX) {
    		throw new IllegalArgumentException(
    				"Invalid difficulty level (" +
    				difficulty +
    				"); must be between " +
    				MIN +
    				" and " + 
    				MAX
    		);
    	}

    	this.difficulty = difficulty;
    }

    public GameType getGameType() {
    	return gameType;
    }

    public void setGameType(GameType gameType) {
    	this.gameType = gameType;
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