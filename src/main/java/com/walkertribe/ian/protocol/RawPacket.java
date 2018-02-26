package com.walkertribe.ian.protocol;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.util.TextUtil;

/**
 * Any packet that IAN has not parsed. This may be because it was not recognized
 * by any registered protocol, or because there are no registered packet
 * listeners that are interested in it.
 * @author rjwut
 */
public abstract class RawPacket extends BaseArtemisPacket {
    protected final byte[] mPayload;

    protected RawPacket(Origin origin, int type, byte[] payload) {
    	mOrigin = origin;
    	mType = type;
    	mPayload = payload;
    }

    /**
     * Returns the payload for this packet.
     */
    public byte[] getPayload() {
    	return mPayload;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
    	writer.writeBytes(mPayload);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("0x").append(TextUtil.intToHex(getType())).append(' ')
			.append(TextUtil.byteArrayToHexString(mPayload));
	}
}