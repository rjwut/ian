package com.walkertribe.ian.util;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

/**
 * A 3d grid coordinate, for referencing internal systems on the Player's ship.
 * @author dhleong
 */
public final class GridCoord implements Comparable<GridCoord> {
    private static final int CACHE_SIZE = 50;
    private static final Queue<GridCoord> sCache = new ArrayDeque<GridCoord>(CACHE_SIZE);

    private final int x, y, z;

    private GridCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * The X-coordinate value
     */
    public int getX() {
    	return x;
    }

    /**
     * The Y-coordinate value
     */
    public int getY() {
    	return y;
    }

    /**
     * The Z-coordinate value
     */
    public int getZ() {
    	return z;
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof GridCoord)) {
            return false;
        }

        GridCoord cast = (GridCoord) other;
        return equals(cast.x, cast.y, cast.z);
    }

    /**
     * Returns true if the given coordinates match those of this object.
     */
    public final boolean equals(int ox, int oy, int oz) {
        return (x == ox && y == oy && z == oz);
    }

    @Override
    public int hashCode() {
    	return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
    	return new StringBuilder()
    		.append('[')
    		.append(x)
    		.append(',')
    		.append(y)
    		.append(',')
    		.append(z)
    		.append(']')
    		.toString();
    }

    /**
     * This factory method uses a very simple LRU queue to maintain a cache of
     * GridCoords, since we will probably reuse just a handful but fairly often.
     * This should keep our memory footprint to a minimum.
     */
    public static final GridCoord getInstance(int x, int y, int z) {
        synchronized(sCache) {
            Iterator<GridCoord> iter = sCache.iterator();

            while (iter.hasNext()) {
                GridCoord c = iter.next();

                if (c.equals(x, y, z)) {
                    iter.remove(); // pop out so we can move it to the head
                    sCache.offer(c);
                    return c;
                }
            }
        }

        GridCoord c = new GridCoord(x, y, z);

        // put it in the queue, if there's room. 
        int size = sCache.size();

        if (size >= CACHE_SIZE) {
            synchronized(sCache) {
                sCache.poll(); // free up space
            }
        }

        synchronized(sCache) {
            sCache.offer(c);
        }

        return c;
    }

    /**
     * Sorts in z order, then x, then y.
     */
    @Override
    public int compareTo(GridCoord other) {
        if (z != other.z) {
            return z - other.z;
        }
        
        if (x != other.x) {
            return x - other.x;
        }
        
        return y - other.y;
    }
}