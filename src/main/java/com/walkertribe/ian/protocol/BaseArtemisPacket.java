package com.walkertribe.ian.protocol;

import java.io.IOException;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.Debugger;
import com.walkertribe.ian.iface.PacketWriter;

/**
 * Implements common packet functionality.
 */
public abstract class BaseArtemisPacket implements ArtemisPacket {
	/**
	 * Causes the packet's payload to be written to the given PacketWriter.
	 */
    protected abstract void writePayload(PacketWriter writer);

    /**
     * Writes packet type-specific details (debug info) to be written to the
     * given StringBuilder.
     */
    protected abstract void appendPacketDetail(StringBuilder b);

    private final ConnectionType mConnectionType;
    private final int mType;

    public BaseArtemisPacket(ConnectionType connectionType, PacketType packetType) {
    	this(connectionType, packetType.getHash());
    }

    public BaseArtemisPacket(ConnectionType connectionType, int packetType) {
        mConnectionType = connectionType;
        mType = packetType;
    }

    @Override
    public ConnectionType getConnectionType() {
        return mConnectionType;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public final void writeTo(PacketWriter writer, Debugger debugger) throws IOException {
    	writer.start(mConnectionType, mType);
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