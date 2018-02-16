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

	private List<CharSequence> mText = new LinkedList<CharSequence>();

	private GameOverReasonPacket(PacketReader reader) {
    	super(SubType.GAME_OVER_REASON, reader);

        while (reader.hasMore()) {
        	mText.add(reader.readString());
        }
    }

    public GameOverReasonPacket(CharSequence... text) {
    	super(SubType.GAME_OVER_REASON);

    	if (text.length == 0) {
    		throw new IllegalArgumentException("You must provide a reason");
    	}

    	for (CharSequence line : text) {
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
    public List<CharSequence> getText() {
    	return mText;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);

		for (CharSequence line : mText) {
			writer.writeString(line);
		}
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		for (CharSequence line : mText) {
			b.append("\n\t").append(line);
		}
	}
}