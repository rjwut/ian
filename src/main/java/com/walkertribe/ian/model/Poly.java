package com.walkertribe.ian.model;

import java.util.Collection;

/**
 * Represents a single polygon in the Vessel's model.
 * @author rjwut
 */
public class Poly {
	String[] vertexIds;

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
}