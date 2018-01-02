package com.walkertribe.ian.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class JamCrcTest {
	private static final Map<Integer, String> TYPES = new LinkedHashMap<Integer, String>();

	static {
		TYPES.put(0x0351a5ac, "valueFloat");
		TYPES.put(0x077e9f3c, "shipSystemSync");
		TYPES.put(0x19c6e2d4, "clientConsoles");
		TYPES.put(0x26faacb9, "gmButton");
		TYPES.put(0x3de66711, "startGame");
		TYPES.put(0x4c821d3c, "valueInt");
		TYPES.put(0x574c4c4b, "commsMessage");
		TYPES.put(0x69cc01d9, "valueFourInts");
		TYPES.put(0x6aadc57f, "controlMessage");
		TYPES.put(0x6d04b3da, "plainTextGreeting");
		TYPES.put(0x80803df9, "objectBitStream");
		TYPES.put(0x809305a7, "gmText");
		TYPES.put(0x902f0b1a, "bigMess");
		TYPES.put(0x9ad1f23b, "carrierRecord");
		TYPES.put(0xae88e058, "incomingMessage");
		TYPES.put(0xb83fd2c4, "attack");
		TYPES.put(0xc2bee72e, "beamRequest");
		TYPES.put(0xcc5a3e30, "objectDelete");
		TYPES.put(0xd672c35f, "commText");
		TYPES.put(0xe548e74a, "connected");
		TYPES.put(0xee665279, "objectText");
		TYPES.put(0xf5821226, "heartbeat");
		TYPES.put(0xf754c8fe, "simpleEvent");
	}

	@Test
	public void testPacketTypes() {
		for (Map.Entry<Integer, String> entry : TYPES.entrySet()) {
			Assert.assertEquals(entry.getKey(), Integer.valueOf(JamCrc.compute(entry.getValue())));
		}
	}

	@Test
	public void makeEclEmmaHappy() {
		TestUtil.coverPrivateConstructor(JamCrc.class);
	}
}