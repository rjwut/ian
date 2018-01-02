package com.walkertribe.ian.protocol.core.comm;

import com.walkertribe.ian.enums.AudioCommand;
import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Plays or deletes an audio message.
 */
public class AudioCommandPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.CONTROL_MESSAGE;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return AudioCommandPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new AudioCommandPacket(reader);
			}
		});
	}

    private int audioId;
    private AudioCommand cmd;

    /**
     * @param audioId The ID of the audio message to which the command applies
     * @param cmd The command to issue (PLAY or DELETE)
     */
    public AudioCommandPacket(int audioId, AudioCommand cmd) {
        super(ConnectionType.CLIENT, TYPE);

        if (cmd == null) {
        	throw new IllegalArgumentException("You must provide a command");
        }

        this.audioId = audioId;
        this.cmd = cmd;
    }

    private AudioCommandPacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
    	audioId = reader.readInt();
    	cmd = AudioCommand.values()[reader.readInt()];
    }

    public int getAudioId() {
    	return audioId;
    }

    public AudioCommand getCommand() {
    	return cmd;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer.writeInt(audioId).writeInt(cmd.ordinal());
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(cmd).append(" msg #").append(audioId);
	}
}