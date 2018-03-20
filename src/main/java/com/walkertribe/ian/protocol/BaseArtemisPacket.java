package com.walkertribe.ian.protocol;

import java.io.IOException;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.Debugger;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.util.JamCrc;

/**
 * Implements common packet functionality.
 */
public abstract class BaseArtemisPacket implements ArtemisPacket {
	/**
	 * Determines the packet type hash specified in the given Packet annotation. 
	 */
	public static int getHash(Packet anno) {
		String typeName = anno.type();

		if (typeName.length() != 0) {
			return JamCrc.compute(typeName);
		}

		int type = anno.hash();

		if (type == 0) {
			throw new RuntimeException("@Packet must have either a type or a hash");
		}

		return type;
	}

	/**
	 * Causes the packet's payload to be written to the given PacketWriter.
	 */
    protected abstract void writePayload(PacketWriter writer);

    /**
     * Writes packet type-specific details (debug info) to be written to the
     * given StringBuilder.
     */
    protected abstract void appendPacketDetail(StringBuilder b);

    protected Origin mOrigin;
    protected int mType;
    private long mTimestamp;

    public BaseArtemisPacket() {
    	Packet anno = getClass().getAnnotation(Packet.class);

    	if (anno != null) {
        	mOrigin = anno.origin();
            mType = getHash(anno);
    	} else if (!(this instanceof RawPacket)) {
    		throw new RuntimeException(getClass() + " must have a @Packet annotation");
    	}

    	mTimestamp = System.nanoTime();
    }

    @Override
    public Origin getOrigin() {
        return mOrigin;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public long getTimestamp() {
    	return mTimestamp;
    }

    @Override
    public final void writeTo(PacketWriter writer, Debugger debugger) throws IOException {
    	writer.start(mOrigin, mType);
    	writePayload(writer);
    	writer.flush(debugger);
    }

    @Override
    public final String toString() {
    	StringBuilder b = new StringBuilder();
    	b.append('[').append(getClass().getSimpleName()).append("] ");
    	appendPacketDetail(b);
    	return b.toString();
    }
}