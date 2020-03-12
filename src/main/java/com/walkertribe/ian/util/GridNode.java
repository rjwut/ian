package com.walkertribe.ian.util;

public abstract class GridNode {
    protected GridCoord coord;

    protected GridNode(GridCoord coord) {
        this.coord = coord;
    }

    public GridCoord getCoord() {
        return coord;
    }

    @Override
    public String toString() {
        return coord.toString();
    }
}
