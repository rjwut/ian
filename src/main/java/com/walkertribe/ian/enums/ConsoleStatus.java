package com.walkertribe.ian.enums;

/**
 * Availability status for the various bridge consoles.
 * @author rjwut
 */
public enum ConsoleStatus {
	AVAILABLE,   // you can claim this station
	YOURS,       // you have already claimed this station
	UNAVAILABLE; // you cannot claim this station
}