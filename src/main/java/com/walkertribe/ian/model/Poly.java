package com.walkertribe.ian.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a single polygon in the Vessel's model.
 * @author rjwut
 */
public class Poly implements Iterable<String> {
	private String[] pointIds;

	/**
	 * Creates a new Poly using the given point IDs.
	 */
	Poly(Collection<String> pointIds) {
		this.pointIds = new String[pointIds.size()];
		int i = 0;

		for (String vert : pointIds) {
			this.pointIds[i++] = vert;
		}
	}

	public String getPointId(int index) {
	    return pointIds[index];
	}

	public int pointCount() {
	    return pointIds.length;
	}

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(pointIds).iterator();
    }
}