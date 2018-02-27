package com.walkertribe.ian.protocol.core.comm;

import com.walkertribe.ian.enums.AudioCommand;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Plays or deletes an audio message.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.CONTROL_MESSAGE)
public class AudioCommandPacket extends BaseArtemisPacket {
    private int audioId;
    private AudioCommand cmd;

    public AudioCommandPacket(int audioId, AudioCommand cmd) {
        if (cmd == null) {
        	throw new IllegalArgumentException("You must provide a command");
        }

        this.audioId = audioId;
        this.cmd = cmd;
    }

    public AudioCommandPacket(PacketReader reader) {
    	audioId = reader.readInt();
    	cmd = AudioCommand.values()[reader.readInt()];
    }

    /**
     * The ID of the audio message to which the command applies.
     */
    public int getAudioId() {
    	return audioId;
    }

    /**
     * The action to perform with that message.
     */
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