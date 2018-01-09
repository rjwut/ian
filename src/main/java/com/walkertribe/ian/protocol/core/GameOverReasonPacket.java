package com.walkertribe.ian.protocol.core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

public class GameOverReasonPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.GAME_OVER_REASON, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameOverReasonPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameOverReasonPacket(reader);
			}
		});
	}

	private List<String> mText;

	private GameOverReasonPacket(PacketReader reader) {
    	super(SubType.GAME_OVER_REASON, reader);
        mText = new LinkedList<String>();

        while (reader.hasMore()) {
        	mText.add(reader.readString());
        }
    }

    public GameOverReasonPacket(String... text) {
    	super(SubType.GAME_OVER_REASON);

    	if (text.length == 0) {
    		throw new IllegalArgumentException("You must provide a reason");
    	}

    	for (String line : text) {
    		if (line == null || line.length() == 0) {
    			throw new IllegalArgumentException("No blank lines allowed");
    		}
    	}

    	mText = Arrays.asList(text);
    }

    /**
     * The text describing why the game ended. Each String in the List is one
     * line.
     */
    public List<String> getText() {
        return new LinkedList<String>(mText);
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);

		for (String line : mText) {
			writer.writeString(line);
		}
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		for (String line : mText) {
			b.append("\n\t").append(line);
		}
	}
}