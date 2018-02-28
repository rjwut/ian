package com.walkertribe.ian.protocol.core.comm;

import com.walkertribe.ian.enums.AudioMode;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Received when an incoming COMMs audio message arrives.
 * @author dhleong
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.INCOMING_MESSAGE)
public class IncomingAudioPacket extends BaseArtemisPacket {
    private final int mId;
    private final AudioMode mMode;
    private final CharSequence mTitle;
    private final CharSequence mFile;

    /**
     * Indicates that the audio message with the given ID has started playing
     * (Mode.PLAYING).
     */
    public IncomingAudioPacket(int id) {
    	mId = id;
    	mMode = AudioMode.PLAYING;
        mTitle = null;
        mFile = null;
    }

    /**
     * Indicates that there is an incoming audio message (Mode.INCOMING).
     */
    public IncomingAudioPacket(int id, String title, String file) {
    	mId = id;
    	mMode = AudioMode.INCOMING;
        mTitle = title;
        mFile = file;
    }

    public IncomingAudioPacket(PacketReader reader) {
        mId = reader.readInt();
        mMode = AudioMode.values()[reader.readInt() - 1];

        if (mMode == AudioMode.INCOMING) {
            mTitle = reader.readString();
            mFile = reader.readString();
        } else {
        	mTitle = null;
        	mFile = null;
        }
    }

    /**
     * The ID assigned to this audio message.
     */
    public int getAudioId() {
        return mId;
    }

    /**
     * The file name for this audio message. This will only be populated if
     * getAudioMode() returns IncomingAudioPacket.Mode.INCOMING; otherwise, it
     * returns null.
     */
    public CharSequence getFileName() {
        return mFile;
    }
    
    /**
     * The title for this audio message. This will only be populated if
     * getAudioMode() returns IncomingAudioPacket.Mode.INCOMING; otherwise, it
     * returns null.
     */
    public CharSequence getTitle() {
        return mTitle;
    }

    @Override
    public Origin getOrigin() {
        return Origin.SERVER;
    }
    
    /**
     * Indicates whether this packet indicates that the message is available
     * (INCOMING) or playing (PLAYING).
     */
    public AudioMode getAudioMode() {
        return mMode;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mId);
		writer.writeInt(mMode.ordinal() + 1);

        if (mMode == AudioMode.INCOMING) {
            writer.writeString(mTitle);
            writer.writeString(mFile);
        }
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mId).append(' ').append(mMode);

		if (mMode == AudioMode.INCOMING) {
			b.append(": ").append(mTitle);
		}
	}
}