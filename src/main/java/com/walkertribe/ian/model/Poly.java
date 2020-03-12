package com.walkertribe.ian.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a single polygon in the Vessel's model.
 * @author rjwut
 */
public class Poly implements Iterable<String> {
	private String[] vertexIds;

	/**
	 * Creates a new Poly using the given vertex IDs.
	 */
	Poly(Collection<String> vertexIds) {
		this.vertexIds = new String[vertexIds.size()];
		int i = 0;

		for (String vert : vertexIds) {
			this.vertexIds[i++] = vert;
		}
	}

	public String getVertexId(int index) {
	    return vertexIds[index];
	}

	public int vertexCount() {
	    return vertexIds.length;
	}

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(vertexIds).iterator();
    }
}