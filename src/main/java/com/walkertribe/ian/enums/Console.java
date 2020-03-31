package com.walkertribe.ian.enums;

/**
 * The bridge consoles.
 * @author rjwut
 */
public enum Console {
	MAIN_SCREEN("Main screen", ClaimType.NO_LIMIT),
	HELM("Helm", ClaimType.ONE_PER_SHIP),
	WEAPONS("Weapons", ClaimType.ONE_PER_SHIP),
	ENGINEERING("Engineering", ClaimType.ONE_PER_SHIP),
	SCIENCE("Science", ClaimType.NO_LIMIT),
	COMMUNICATIONS("Communications", ClaimType.NO_LIMIT),
	SINGLE_SEAT_CRAFT("Single-seat craft", ClaimType.NO_LIMIT),
	DATA("Data", ClaimType.NO_LIMIT),
	OBSERVER("Observer", ClaimType.NO_LIMIT),
	CAPTAINS_MAP("Captain's map", ClaimType.NO_LIMIT),
	GAME_MASTER("Game master", ClaimType.ONE_PER_GAME);

    /**
     * How this Console can be claimed
     */
    public enum ClaimType {
        /**
         * Only one client may claim this Console per ship.
         */
        ONE_PER_SHIP,

        /**
         * This Console can be claimed multiple times.
         */
        NO_LIMIT,

        /**
         * Only one client may claim this Console per game.
         */
        ONE_PER_GAME
    }

    private String label;
    private ClaimType claimType;

	Console(String label, ClaimType claimType) {
		this.label = label;
		this.claimType = claimType;
	}

	/**
	 * Returns a ClaimType value which indicates how this Console may be claimed.
	 */
	public ClaimType getClaimType() {
	    return claimType;
	}

	@Override
	public String toString() {
		return label;
	}
}