package com.walkertribe.ian.protocol.core.eng;

import java.util.Objects;

import com.walkertribe.ian.util.GridCoord;
import com.walkertribe.ian.util.GridNode;

public class NodeDamage extends GridNode {
    private float damage;

    protected NodeDamage(GridCoord coord, float damage) {
        super(coord);
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage  = damage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof NodeDamage)) {
            return false;
        }

        NodeDamage that = (NodeDamage) obj;
        return Objects.equals(coord, that.coord) && damage == that.damage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, damage);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + damage;
    }
}
