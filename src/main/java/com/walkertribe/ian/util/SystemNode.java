package com.walkertribe.ian.util;

import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.vesseldata.VesselNode;

/**
 * Context-aware extension of GridNode. Can report on ship coordinates and the ship system.
 * @author rjwut
 */
public class SystemNode extends GridNode {
    private SystemGrid grid;
    private VesselNode internalsNode;
    private float damage;

    public SystemNode(SystemGrid grid, GridCoord coord) {
        super(coord);
        this.grid = grid;
    }

    public float getShipX() {
        getInternalsNode();
        return internalsNode != null ? internalsNode.getShipX() : Float.MIN_VALUE;
    }

    public float getShipY() {
        getInternalsNode();
        return internalsNode != null ? internalsNode.getShipY() : Float.MIN_VALUE;
    }

    public float getShipZ() {
        getInternalsNode();
        return internalsNode != null ? internalsNode.getShipZ() : Float.MIN_VALUE;
    }

    public Boolean isAccessible() {
        getInternalsNode();
        return internalsNode == null ? Boolean.valueOf(internalsNode.isAccessible()) : null;
    }

    public ShipSystem getSystem() {
        getInternalsNode();
        return internalsNode != null ? internalsNode.getSystem() : null;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    private VesselNode getInternalsNode() {
        if (internalsNode == null && grid.internals != null) {
            internalsNode = grid.internals.get(coord);
        }

        return internalsNode;
    }
}
