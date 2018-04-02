package com.walkertribe.ian.protocol.core;

/**
 * Defines the known packet types for the core Artemis protocol.
 * @author rjwut
 */
public final class CorePacketType {
	public static final String ATTACK = "attack";
	public static final String BEAM_REQUEST = "beamRequest";
	public static final String BIG_MESS = "bigMess";
	public static final String CARRIER_RECORD = "carrierRecord";
	public static final String CLIENT_CONSOLES = "clientConsoles";
	public static final String COMMS_BUTTON = "commsButton";
	public static final String COMMS_MESSAGE = "commsMessage";
	public static final String COMM_TEXT = "commText";
	public static final String CONNECTED = "connected";
	public static final String CONTROL_MESSAGE = "controlMessage";
	public static final String GM_BUTTON = "gmButton";
	public static final String GM_TEXT = "gmText";
	public static final String HEARTBEAT = "heartbeat";
	public static final String IDLE_TEXT = "idleText";
	public static final String INCOMING_MESSAGE = "incomingMessage";
	public static final String OBJECT_BIT_STREAM = "objectBitStream";
	public static final String OBJECT_DELETE = "objectDelete";
	public static final String OBJECT_TEXT = "objectText";
	public static final String PLAIN_TEXT_GREETING = "plainTextGreeting";
	public static final String SIMPLE_EVENT = "simpleEvent";
	public static final String SHIP_SYSTEM_SYNC = "shipSystemSync";
	public static final String START_GAME = "startGame";
	public static final String VALUE_FLOAT = "valueFloat";
	public static final String VALUE_FOUR_INTS = "valueFourInts";
	public static final String VALUE_INT = "valueInt";

	private CorePacketType() {
		// prevent instantiation
	}
}
