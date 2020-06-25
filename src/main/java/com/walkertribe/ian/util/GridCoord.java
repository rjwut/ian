package com.walkertribe.ian.util;

import java.util.Iterator;

/**
 * An engineering nodes coordinate.
 * @author rjwut
 */
public final class GridCoord implements Comparable<GridCoord> {
    public static final int MAX_X = 5;
    public static final int MAX_Y = 5;
    public static final int MAX_Z = 10;
    public static final int LENGTH = MAX_X * MAX_Y * MAX_Z;

    private static final GridCoord[] COORDS = new GridCoord[LENGTH];

    static {
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                for (int z = 0; z < MAX_Z; z++) {
                    GridCoord coord = new GridCoord(x, y, z);
                    COORDS[coord.index] = coord;
                }
            }
        }
    }

    /**
     * Computes the index value for the given coordinates.
     */
    public static int computeIndex(int x, int y, int z) {
        if (x < 0 || x >= MAX_X || y < 0 || y >= MAX_Y || z < 0 || z >= MAX_Z) {
            throw new IllegalArgumentException(
                    "Coordinates out of range: (" + x + "," + y + "," + z + ")"
            );
        }

        return x * MAX_Y * MAX_Z + y * MAX_Z + z;
    }

    /**
     * Returns the GridCoord object that corresponds to the given coordinates.
     */
    public static GridCoord get(int x, int y, int z) {
        return COORDS[computeIndex(x, y, z)];
    }

    /**
     * Returns an Iterator for all GridCoords.
     */
    public static Iterator<GridCoord> iterator() {
        return new Iterator<GridCoord>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < LENGTH;
            }

            @Override
            public GridCoord next() {
                return COORDS[i++];
            }
        };
    }

    protected int x;
    protected int y;
    protected int z;
    protected int index;

    private GridCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        index = computeIndex(x, y, z);
    }

    /**
     * The X value of this coordinate.
     */
    public int x() {
        return x;
    }

    /**
     * The Y value of this coordinate.
     */
    public int y() {
        return y;
    }

    /**
     * The Z value of this coordinate.
     */
    public int z() {
        return z;
    }

    /**
     * Returns the index value for this GridCoord.
     */
    public int index() {
        return index;
    }

    /**
     * Returns true if this object is located at the given coordinates.
     */
    public boolean at(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof GridCoord)) {
            return false;
        }

        return index == ((GridCoord) obj).index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }

    @Override
    public int compareTo(GridCoord o) {
        return index - o.index;
    }
}
