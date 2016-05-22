package com.walkertribe.ian.model;

import java.awt.Polygon;
import java.util.Collection;
import java.util.Map;

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

	/**
	 * Returns a Polygon object projected onto a 2D viewing plane for this Poly.
	 */
	Polygon toPolygon(Map<String, double[]> vertexMap, RenderParams config) {
		int[] x = new int[vertexIds.length];
		int[] y = new int[vertexIds.length];

		for (int i = 0; i < vertexIds.length; i++) {
			String vertex = vertexIds[i];
			double[] p = vertexMap.get(vertex);
			x[i] = (int) Math.round(p[0]);
			y[i] = (int) Math.round(p[2]);
		}

		return new Polygon(x, y, vertexIds.length);
	}
}