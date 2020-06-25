package com.walkertribe.ian.model;

import java.util.List;

import com.walkertribe.ian.util.Matrix;

/**
 * A location in 3D space in a Model.
 * @author rjwut
 */
public class Point {
	private double x;
	private double y;
	private double z;

	/**
	 * Construct a new Point at the given position.
	 */
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = -z; // flip to compensate for inverted y axis on screen
	}

	/**
	 * Returns this Point's distance from the origin.
	 */
	double r() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Returns a double array containing this Point's position.
	 */
	public double[] getCoordinates() {
		return new double[] { x, y, z };
	}

	/**
	 * Returns a new Point that represents a point between this Point and the given Point. How
	 * close the returned Point is to the other two is determined by the fraction argument. A
	 * fraction of 0 returns a Point at the same location as this Point, while a fraction of 1
	 * returns a Point at the same location as the given Point. A value between 0 and 1 produces
	 * a Point that is positioned proportionally along the line between the two other Point
	 * objects.
	 */
	public Point getBetween(Point other, float fraction) {
	    return new Point(
                (other.x - x) * fraction + x,
                (other.y - y) * fraction + y,
                -((other.z - z) * fraction + z)
        );
	}

	/**
	 * Applies the given List of Matrix objects to this Point and returns a
	 * double array containing the transformed Point's position. 
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