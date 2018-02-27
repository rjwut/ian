package com.walkertribe.ian.enums;

/**
 * Messages that can be sent to civilian NPCs.
 * @author rjwut 
 */
public enum OtherMessage implements CommsMessage {
	HAIL(0),
	TURN_TO_HEADING_0(1),
	TURN_TO_HEADING_90(2),
	TURN_TO_HEADING_180(3),
	TURN_TO_HEADING_270(4),
	TURN_LEFT_10_DEGREES(5),
	TURN_RIGHT_10_DEGREES(6),
	TURN_LEFT_25_DEGREES(15),
	TURN_RIGHT_25_DEGREES(16),
	ATTACK_NEAREST_ENEMY(7),
	PROCEED_TO_YOUR_DESTINATION(8),
	GO_DEFEND(9) {
		@Override
		public boolean hasArgument() {
			return true;
		}
	};

	/**
	 * Returns the OtherMessage that corresponds to the given ID.
	 */
	public static OtherMessage fromId(int id) {
		for (OtherMessage message : values()) {
			if (message.id == id) {
				return message;
			}
		}

		return null;
	}

	private int id;

	OtherMessage(int id) {
		this.id = id;
	}

	@Override
	public boolean hasArgument() {
		return false;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public CommsRecipientType getRecipientType() {
		return CommsRecipientType.OTHER;
	}
}