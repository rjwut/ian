package com.walkertribe.ian.util;

import java.util.ArrayList;
import java.util.List;

import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.protocol.core.EndGamePacket;

/**
 * Tracks the state of the ship's system grid.
 * @author rjwut
 */
public abstract class Grid<N extends GridNode> {
    public abstract N newNode(GridCoord coord);

    protected List<N> grid = new ArrayList<>(GridCoord.LENGTH);

    @Listener
    public void onEndGame(EndGamePacket pkt) {
        grid.clear();
    }

    /**
     * Returns the GridCoord at the given grid coordinates.
     */
    public N get(GridCoord coord) {
        return grid.get(coord.index());
    }

    /**
     * Sets the GridCoord in the grid.
     */
    public void set(N node) {
        grid.set(node.getCoord().index(), node);
    }
}
