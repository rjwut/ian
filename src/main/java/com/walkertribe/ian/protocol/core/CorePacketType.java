package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.util.JamCrc;

/**
 * PacketType implementation for the core Artemis protocol.
 * @author rjwut
 */
public enum CorePacketType implements PacketType {
	ATTACK,
	BEAM_REQUEST,
	BIG_MESS,
	CARRIER_RECORD,
	CLIENT_CONSOLES,
	COMMS_MESSAGE,
	COMM_TEXT,
	CONNECTED,
	CONTROL_MESSAGE,
	GM_BUTTON,
	GM_TEXT,
	HEARTBEAT,
	INCOMING_MESSAGE,
	OBJECT_BIT_STREAM,
	OBJECT_DELETE,
	OBJECT_TEXT,
	PLAIN_TEXT_GREETING,
	SIMPLE_EVENT,
	SHIP_SYSTEM_SYNC,
	START_GAME,
	VALUE_FLOAT,
	VALUE_FOUR_INTS,
	VALUE_INT;

	private String internalName;
	private int hash;

	CorePacketType() {
		internalName = constantNameToCamelCase(name());
		hash = JamCrc.compute(internalName);
	}

	@Override
	public String getInternalName() {
		return internalName;
	}

	@Override
	public int getHash() {
		return hash;
	}

	/**
	 * Given a name in constant format (THIS_IS_A_CONSTANT), returns its camelCase equivalent (thisIsAConstant).
	 */
	private static String constantNameToCamelCase(String str) {
		StringBuilder cc = new StringBuilder();

		for (String part : str.split("_")) {
			char c = part.charAt(0);
			cc.append(cc.length() == 0 ? Character.toLowerCase(c) : Character.toUpperCase(c));
			cc.append(part.substring(1, part.length()).toLowerCase());
		}

		return cc.toString();
	}
}
