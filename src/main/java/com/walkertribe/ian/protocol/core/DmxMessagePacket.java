package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;
import com.walkertribe.ian.util.Util;

/**
 * Updates the state of a DMX flag.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.DMX_MESSAGE)
public class DmxMessagePacket extends SimpleEventPacket {
    private final CharSequence mName;
    private final boolean mOn;

    public DmxMessagePacket(String name, boolean on) {
        if (Util.isBlank(name)) {
        	throw new IllegalArgumentException("You must provide a name");
        }

        mName = name;
        mOn = on;
    }

    public DmxMessagePacket(PacketReader reader) {
        super(reader);
        mName = reader.readString();
        mOn = reader.readInt() == 1;
    }

    /**
     * The name of the DMX flag.
     */
    public CharSequence getName() {
        return mName;
    }

    /**
     * Returns true if the DMX flag is on; false otherwise.
     */
    public boolean isOn() {
    	return mOn;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	super.writePayload(writer);
		writer.writeString(mName).writeInt(mOn ? 1 : 0);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mName).append('=').append(mOn ? "ON" : "OFF");
	}
}