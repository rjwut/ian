package com.walkertribe.ian.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.walkertribe.ian.util.Matrix;

/**
 * Contains data about the Vessel's model.
 * @author rjwut
 */
public class Model {

    /**
     * Rotates, scales and translates a point cloud, then returns a Map describing the resulting
     * locations of all given Points in 3D space. Each point is declared as an array containing
     * three doubles: x, y, z.
     */
    public static Map<String, double[]> transformPoints(Map<String, Point> points, RenderParams config) {
        Map<String, double[]> pointMap = new HashMap<String, double[]>();
        List<Matrix> matrices = new LinkedList<Matrix>();

        if (!withinEpsilon(config.mScale, 1.0)) {
            matrices.add(Matrix.buildScaleMatrix(config.mScale));
        }

        if (!withinEpsilon(config.mRotateX, 0)) {
            matrices.add(Matrix.buildRotateXMatrix(config.mRotateX));
        }

        if (!withinEpsilon(config.mRotateY, 0)) {
            matrices.add(Matrix.buildRotateYMatrix(config.mRotateY));
        }

        if (!withinEpsilon(config.mRotateZ, 0)) {
            matrices.add(Matrix.buildRotateZMatrix(config.mRotateZ));
        }

        for (Map.Entry<String, Point> entry : points.entrySet()) {
            double[] point = entry.getValue().applyMatrices(matrices);
            point[0] += config.mOffsetX;
            point[1] += config.mOffsetY;
            point[2] += config.mOffsetZ;
            pointMap.put(entry.getKey(), point);
        }

        return pointMap;
    }

    private static final float EPSILON = 0.00000001f;

	/**
	 * Returns true if the given double values are close enough to each other to
	 * be considered equivalent (due to rounding errors).
	 */
	private static boolean withinEpsilon(double val1, double val2) {
		return Math.abs(val1 - val2) <= EPSILON;
	}

	private String dxsPaths;
	private double maxRadius = 0;
	private Map<String, Point> pointMap = new HashMap<String, Point>();
	private List<Poly> polys = new LinkedList<Poly>();

	public Model(String dxsPaths) {
		this.dxsPaths = dxsPaths;
	}

	/**
	 * Adds the given Points and Polys to this Model.
	 */
	public void add(Map<String, Point> v, List<Poly> p) {
		pointMap.putAll(v);
		polys.addAll(p);

		for (Point point : v.values()) {
			maxRadius = Math.max(maxRadius, point.r());
		}
	}

	/**
	 * Returns the comma-delimited paths to the .dxs files for this Model,
	 * relative to the Artemis install directory.
	 */
	public String getDxsPaths() {
		return dxsPaths;
	}

	/**
	 * Returns the size of this Model. This is distance from the origin to the
	 * Model's furthest Point.
	 */
	public double getSize() {
		return maxRadius;
	}

	/**
	 * Returns the scale value that can be passed to draw() that will ensure
	 * that the drawn Model fits within the given radius.
	 */
	public double computeScale(double radius) {
		return radius / maxRadius;
	}

	/**
	 * Returns a copy of the Point Map contained in this model. Vertices are
	 * mapped by their IDs.
	 */
	public Map<String, Point> getPointMap() {
		return new HashMap<String, Point>(pointMap);
	}

	/**
	 * Returns a copy of the List of Polys contained in this model.
	 */
	public List<Poly> getPolys() {
		return new ArrayList<Poly>(polys);
	}

	/**
	 * Rotates, scales and translates the Model, then returns a Map describing the resulting
	 * locations of all the Model's points in 3D space. Each point is declared as an array
	 * containing three doubles: x, y, z.
	 */
	public Map<String, double[]> transformPoints(RenderParams config) {
	    return transformPoints(pointMap, config);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Model)) {
			return false;
		}

		return dxsPaths.equals(((Model) object).dxsPaths);
	}

	@Override
	public int hashCode() {
		return dxsPaths.hashCode();
	}
}