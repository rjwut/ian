package com.walkertribe.ian.protocol.core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;

public class GameOverReasonPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.SIMPLE_EVENT;
    private static final byte MSG_TYPE = 0x14;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, MSG_TYPE,
				new PacketFactory() {
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
    	super(ConnectionType.SERVER, TYPE);
        reader.skip(4); // subtype
        mText = new LinkedList<String>();

        while (reader.hasMore()) {
        	mText.add(reader.readString());
        }
    }

    public GameOverReasonPacket(String... text) {
    	super(ConnectionType.SERVER, TYPE);
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
		writer.writeInt(MSG_TYPE);

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