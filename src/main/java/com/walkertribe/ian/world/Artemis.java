package com.walkertribe.ian.world;

import com.walkertribe.ian.enums.ShipSystem;

/**
 * Artemis-related constants
 * @author rjwut
 */
public final class Artemis {
	/**
	 * The default port on which the Artemis server listens for connections.
	 */
	public static final int DEFAULT_PORT = 2010;

	/**
	 * The default amount of coolant the player ship has at start. The amount of
	 * coolant may change due to side mission rewards or custom scripting.
	 */
    public static final byte DEFAULT_COOLANT = 8;

    /**
     * The maximum amount of coolant that can be applied to any one system.
     */
    public static final int MAX_COOLANT_PER_SYSTEM = 8;

    /**
     * The maximum energy allocation for a system, in percentage points.
     */
    public static final int MAX_ENERGY_ALLOCATION_PERCENT = 300;

    /**
     * The maximum number of tubes a ship can have. Note that none of the ships
     * in the stock install of Artemis have this many tubes, but a custom ship
     * might.
     */
    public static final int MAX_TUBES = 6;

    /**
     * The maximum warp factor player ships can achieve.
     */
    public static final byte MAX_WARP = 4;

    /**
     * The number of available player ships.
     */
    public static final int SHIP_COUNT = 8;

    /**
     * The number of ship systems.
     */
    public static final int SYSTEM_COUNT = ShipSystem.values().length;

    /**
     * The length of the sides of the map (the X and Z dimensions).
     */
    public static final int MAP_SIZE = 100000;

    private Artemis() {
		// prevent instantiation of this class
	}
}