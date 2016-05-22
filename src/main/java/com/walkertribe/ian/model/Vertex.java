package com.walkertribe.ian.model;

import java.util.List;

import com.walkertribe.ian.util.Matrix;

/**
 * A point in 3D space in a Model.
 * @author rjwut
 */
public class Vertex {
	private double x;
	private double y;
	private double z;

	/**
	 * Construct a new Vertex at the given position.
	 */
	Vertex(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = -z; // flip to compensate for inverted y axis on screen
	}

	/**
	 * Returns this Vertex's distance from the origin.
	 */
	double r() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Returns a double array containing this Vertex's position.
	 */
	public double[] getCoordinates() {
		return new double[] { x, y, z };
	}

	/**
	 * Applies the given List of Matrix objects to this Vertex and returns a
	 * double array containing the transformed Vertex's position. 
	 */
	public double[] applyMatrices(List<Matrix> matrices) {
		Matrix coords = new Matrix(x, y, z);

		for (Matrix matrix : matrices) {
			coords = matrix.multiply(coords);
		}

		return new double[] {
				coords.get(0, 0),
				coords.get(1, 0),
				coords.get(2, 0)
		};
	}
}