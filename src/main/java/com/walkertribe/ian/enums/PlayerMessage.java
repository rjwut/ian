package com.walkertribe.ian.enums;

/**
 * Messages that can be sent to other players.
 * @author rjwut
 */
public enum PlayerMessage implements CommsMessage {
	YES,
	NO,
	HELP,
	GREETINGS,
	DIE,
	WERE_LEAVING_THE_SECTOR_BYE,
	READY_TO_GO,
	PLEASE_FOLLOW_US,
	WELL_FOLLOW_YOU,
	WERE_BADLY_DAMAGED,
	WERE_HEADED_BACK_TO_THE_STATION,
	SORRY_PLEASE_DISREGARD;

	@Override
	public boolean hasArgument() {
		return false;
	}

	@Override
	public int getId() {
		return ordinal();
	}

	@Override
	public CommsRecipientType getRecipientType() {
		return CommsRecipientType.PLAYER;
	}
}