package com.walkertribe.ian.enums;

/**
 * Messages that can be sent to bases.
 * @author rjwut
 */
public enum BaseMessage implements CommsMessage {
	STAND_BY_FOR_DOCKING("Stand by for docking."),
	PLEASE_REPORT_STATUS("Please report status."),
	BUILD_HOMING_MISSILES("Please build type 1 homing ordnance for us."),
	BUILD_NUKES("Please build type 4 nuke ordnance for us."),
	BUILD_MINES("Please build type 6 mine ordnance for us."),
	BUILD_EMPS("Please build type 9 EMP ordnance for us."),
	BUILD_PSHOCKS("Please build type 8 Pshock ordnance for us.");

	private static final int BUILD_HOMING_MISSILES_ORDINAL = BUILD_HOMING_MISSILES.ordinal();

	/**
	 * Returns the BaseMessage enum value that requests that a base build the
	 * given OrdnanceType. For example, if you pass in OrdnanceType.EMP, build()
	 * will return BaseMessage.BUILD_EMPS.
	 */
	public static BaseMessage build(OrdnanceType type) {
		return values()[type.ordinal() + BUILD_HOMING_MISSILES_ORDINAL];
	}

	private String label;

	BaseMessage(String label) {
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