package com.walkertribe.ian.enums;

/**
 * Messages that can be sent to bases.
 * @author rjwut
 */
public enum BaseMessage implements CommsMessage {
	STAND_BY_FOR_DOCKING_OR_CEASE_OPERATION,
	PLEASE_REPORT_STATUS,
	BUILD_HOMING_MISSILES,
	BUILD_NUKES,
	BUILD_MINES,
	BUILD_EMPS,
	BUILD_PSHOCKS,
	BUILD_BEACONS,
	BUILD_PROBES,
	BUILD_TAGS;

	private static final int BUILD_HOMING_MISSILES_ORDINAL = BUILD_HOMING_MISSILES.ordinal();

	/**
	 * Returns the BaseMessage enum value that requests that a base build the
	 * given OrdnanceType. For example, if you pass in OrdnanceType.EMP, build()
	 * will return BaseMessage.BUILD_EMPS.
	 */
	public static BaseMessage build(OrdnanceType type) {
		return values()[type.ordinal() + BUILD_HOMING_MISSILES_ORDINAL];
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
		return CommsRecipientType.BASE;
	}

	/**
	 * Returns the OrdnanceType value that represents the type of ordnance this
	 * command is asking the base to build, or null if it isn't a request to
	 * build ordnance.
	 */
	public OrdnanceType getOrdnanceType() {
		if (ordinal() < BUILD_HOMING_MISSILES_ORDINAL) {
			return null;
		}

		return OrdnanceType.values()[ordinal() - BUILD_HOMING_MISSILES_ORDINAL];
	}
}