package com.walkertribe.ian.enums;

/**
 * Availability status for the various bridge consoles.
 * @author rjwut
 */
public enum ConsoleStatus {
	AVAILABLE,   // you can claim this console
	YOURS,       // you have already claimed this console
	UNAVAILABLE; // you cannot claim this console
}