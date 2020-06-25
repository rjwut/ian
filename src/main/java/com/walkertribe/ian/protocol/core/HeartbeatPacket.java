package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.protocol.BaseArtemisPacket;

/**
 * Common parent class for ServerHeartbeatPacket and ClientHeartbeatPacket.
 * @author rjwut
 */
public abstract class HeartbeatPacket extends BaseArtemisPacket {
    @Override
    protected void appendPacketDetail(StringBuilder b) {
        // nothing to write
    }
}
