package com.walkertribe.ian.enums;

import com.walkertribe.ian.protocol.core.ClientHeartbeatPacket;
import com.walkertribe.ian.protocol.core.HeartbeatPacket;
import com.walkertribe.ian.protocol.core.ServerHeartbeatPacket;

/**
 * Represents the type of the machine found at the opposite end of a connection.
 * @author rjwut
 */
public enum Origin {
	SERVER {
        @Override
        public Origin opposite() {
            return CLIENT;
        }

        @Override
        public ServerHeartbeatPacket createHeartbeat() {
            return new ServerHeartbeatPacket();
        }
    },
	CLIENT {
        @Override
        public Origin opposite() {
            return SERVER;
        }

        @Override
        public ClientHeartbeatPacket createHeartbeat() {
            return new ClientHeartbeatPacket();
        }
    };

	/**
	 * Returns the Origin that corresponds to the given int value.
	 */
	public static final Origin fromInt(int value) {
		return value == 1 ? SERVER : (value == 2 ? CLIENT : null);
	}

    /**
     * Returns the opposite Origin to this one: SERVER.opposite() returns
     * CLIENT and vice-versa.
     */
    public abstract Origin opposite();

    /**
     * Returns a new HeartbeatPacket appropriate to be sent from machines of this Origin type.
     */
    public abstract HeartbeatPacket createHeartbeat();

	private int val;

	Origin() {
		val = ordinal() + 1;
	}

	/**
	 * Returns the int value for this Origin.
	 */
	public int toInt() {
		return val;
	}
}