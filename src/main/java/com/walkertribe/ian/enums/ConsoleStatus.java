package com.walkertribe.ian.enums;

/**
 * Availability status for the various bridge Consoles.
 * @author rjwut
 */
public enum ConsoleStatus {
    /**
     * You can claim this Console
     */
	AVAILABLE,

	/**
	 * You have already claimed this Console
	 */
	YOURS,

	/**
	 * You cannot claim this Console
	 */
	UNAVAILABLE;
}