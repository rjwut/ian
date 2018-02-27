package com.walkertribe.ian.enums;

/**
 * Messages that can be sent to enemy NPCs.
 * @author rjwut 
 */
public enum EnemyMessage implements CommsMessage {
	WILL_YOU_SURRENDER,
	TAUNT_1,
	TAUNT_2,
	TAUNT_3;

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