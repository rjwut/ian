package com.walkertribe.ian.enums;

/**
 * All messages that can be sent by the comm officer implement this interface.
 * @author rjwut
 */
public interface CommsMessage {
	/**
	 * Returns the ID of this CommsMessage. IDs are unique per
	 * CommsRecipientType.
	 */
	public int getId();

	/**
	 * Returns the CommsTargetType that can recieve this CommsMessage.
	 */
	public CommsRecipientType getRecipientType();

	/**
	 * Returns whether or not this message has an argument.
	 */
	public boolean hasArgument();
}