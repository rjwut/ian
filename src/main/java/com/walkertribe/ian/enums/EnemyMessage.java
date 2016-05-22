package com.walkertribe.ian.enums;

/**
 * Messages that can be sent to enemy NPCs.
 * @author rjwut 
 */
public enum EnemyMessage implements CommsMessage {
	WILL_YOU_SURRENDER("Will you surrender?"),
	TAUNT_1("[Taunt #1]"),
	TAUNT_2("[Taunt #2]"),
	TAUNT_3("[Taunt #3]");

	private String label;

	EnemyMessage(String label) {
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
		return CommsRecipientType.ENEMY;
	}
}