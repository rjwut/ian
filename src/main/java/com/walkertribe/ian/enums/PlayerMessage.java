package com.walkertribe.ian.enums;

/**
 * Messages that can be sent to other players.
 * @author rjwut
 */
public enum PlayerMessage implements CommsMessage {
	YES("Yes."),
	NO("No."),
	HELP("Help!"),
	GREETINGS("Greetings."),
	DIE("Die!"),
	WERE_LEAVING_THE_SECTOR_BYE("We're leaving the sector. Bye."),
	READY_TO_GO("Ready to go."),
	PLEASE_FOLLOW_US("Please follow us."),
	WELL_FOLLOW_YOU("We'll follow you."),
	WERE_BADLY_DAMAGED("We're badly damaged."),
	WERE_HEADED_BACK_TO_THE_STATION("We're headed back to the station."),
	SORRY_PLEASE_DISREGARD("Sorry, please disregard.");

	private String label;

	PlayerMessage(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

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