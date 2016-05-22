package com.walkertribe.ian.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.walkertribe.ian.enums.ShipSystem;

/**
 * Some basic management of the internal systems grid for a Player Ship. This is
 * by no means comprehensive--we don't keep track of pixel coordinates, for
 * example, just the "grid" coordinates--but is just enough so we can get the
 * Engineering console to be more complete.
 * @author dhleong
 */
public class ShipSystemGrid {
    public static class GridEntry {
        private final ShipSystem system;
        private final int index; // index of this system among its types, [0,N)
        
        private GridEntry(ShipSystem system, int index) {
            this.system = system;
            this.index = index;
        }

        public ShipSystem getSystem() {
        	return system;
        }

        public int getIndex() {
        	return index;
        }
    }
    
    private final Map<GridCoord, GridEntry> mSystems = new HashMap<GridCoord, GridEntry>();
    private final int[] mSystemCounts = new int[ShipSystem.values().length];

    /**
     * Get the number of nodes we have of the given ShipSystem.
     */
    public int getSystemCount(ShipSystem sys) {
        return mSystemCounts[sys.ordinal()];
    }

    /**
     * Returns the GridEntry found at the given coordinates.
     */
    public GridEntry getGridAt(GridCoord coord) {
        return mSystems.get(coord);
    }

    /**
     * Convenience method for getGridAt(coord).getSystem().
     */
    public ShipSystem getSystemTypeAt(GridCoord coord) {
        return mSystems.get(coord).system;
    }

    /**
     * Get the set of GridCoords contained on this grid.
     */
    public Set<GridCoord> getCoords() {
        return mSystems.keySet();
    }

    /**
     * Returns all GridCoord objects that pertain to the given ShipSystem.
     */
    public Collection<GridCoord> getCoordsFor(ShipSystem sys) {
        List<GridCoord> coords = new ArrayList<GridCoord>(); 

        for (Entry<GridCoord, GridEntry> e : mSystems.entrySet()) {
            if (e.getValue().system == sys) {
                coords.add(e.getKey());
            }
        }

        return coords;
    }
}