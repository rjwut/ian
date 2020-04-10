package com.walkertribe.ian.util;

import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.model.Point;

/**
 * <p>
 * Represents a single node in a system Grid.
 * </p>
 * <p>
 * A GridNode can contain three types of information:
 * </p>
 * <ul>
 * <li>Its coordinates in the system grid (always present)</li>
 * <li>Infrastructure data: Physical position in the ship and the ship system located there (may be absent)</li>
 * <li>Damage amount (may be absent)</li>
 * </ul>
 * <p>
 * If a node does not have infrastructure data, it will always read as inaccessible, with no ship
 * system and no physical position. If a node does not have damage data, the damage value will be
 * -1.0.
 * </p>
 * @author rjwut
 */
public class GridNode {
    private Grid grid;
    protected GridCoord coord;
    private boolean accessible;
    private ShipSystem system;
    private Point point;
    private float damage = -1;

    /**
     * Creates a GridNode that has no infrastructure data. This can be used for a Grid that only
     * tracks status data, or whose infrastructure data will be added dynamically.
     */
    public GridNode(Grid grid, GridCoord coord) {
        this.coord = coord;
    }

    /**
     * Creates a non-system GridNode with a physical location. If accessible is true, the node is a
     * "hallway"; otherwise, it's an empty node.
     */
    public GridNode(Grid grid, GridCoord coord, float x, float y, float z, boolean accessible) {
        this(grid, coord, x, y, z);
        this.accessible = accessible;
    }

    /**
     * Creates a system GridNode.
     */
    public GridNode(Grid grid, GridCoord coord, float x, float y, float z, ShipSystem system) {
        this(grid, coord, x, y, z, true);
        this.system = system;
    }

    /**
     * Common constructor for GridNodes with physical locations.
     */
    private GridNode(Grid grid, GridCoord coord, float x, float y, float z) {
        this(grid, coord);
        point = new Point(x, y, z);
    }

    /**
     * Copies the given GridNode for use in a new Grid. Damage values don't copy.
     */
    public GridNode(Grid grid, GridNode node) {
        this.grid = grid;
        coord = node.coord;
        accessible = node.accessible;
        system = node.system;
        point = node.point;
    }

    /**
     * Returns the GridCoord that specifies this GridNode's location in the system Grid.
     */
    public GridCoord getCoord() {
        return coord;
    }

    /**
     * Returns this GridNode's physical location relative to the center of the ship, or null if it
     * is unknown.
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Returns true if this GridNode is accessible to DAMCON teams. Accessible nodes have either a
     * ship system located there or are a hallway. Inaccessible nodes are typically nodes in the
     * system Grid that fall outside the ship. If infrastructure data isn't included in this node,
     * this method returns false.
     */
    public boolean isAccessible() {
        return accessible;
    }

    /**
     * Returns the ShipSystem value that indicates what ship system is located at this GridNode, or
     * null if no ship system is located here. This method will also return null if no
     * infrastructure data is stored in this node.
     */
    public ShipSystem getSystem() {
        return system;
    }

    /**
     * Returns how much damage there is at this GridNode. If no damage data is stored in this node,
     * this will return -1.
     */
    public float getDamage() {
        return damage;
    }

    /**
     * Sets the amount of damage at this GridNode. This will throw an IllegalArgumentException if
     * the Grid is locked or if the damage value is less than 0.
     */
    public void setDamage(float damage) {
        if (grid != null) {
            grid.assertUnlocked();
        }

        if (damage < 0) {
            throw new IllegalArgumentException("Invalid damage value: " + damage);
        }

        this.damage = damage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof GridNode)) {
            return false;
        }

        GridNode that = (GridNode) obj;
        return grid == that.grid && coord.equals(that.coord);
    }

    @Override
    public int hashCode() {
        return coord.index();
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(coord.toString());

        if (system != null) {
            b.append('{').append(system).append('}');
        }

        if (damage >= 0) {
            b.append(" - ").append(damage).append(" damage");
        }

        return b.toString();
    }
}
