package com.walkertribe.ian.model;

/**
 * Dictates how a model will be rendered.
 */
public class RenderParams {
	double mScale = 1.0;
	double mRotateX;
	double mRotateY;
	double mRotateZ;
	double mOffsetX;
	double mOffsetY;
	double mOffsetZ;

	/**
     * Returns the scaling factor: a value greater than 1 will scale it up,
     * and less than 1 will scale it down.
	 */
	public double scale() {
	    return mScale;
	}

	/**
	 * Sets the scaling factor: a value greater than 1 will scale it up, and
	 * less than 1 will scale it down.
	 */
	public RenderParams scale(double scale) {
		mScale = scale;
		return this;
	}

	/**
	 * Rotates the model around the X-axis.
	 */
	public RenderParams rotateX(double rotateX) {
		mRotateX = rotateX;
		return this;
	}

	/**
	 * Rotates the model around the Y-axis.
	 */
	public RenderParams rotateY(double rotateY) {
		mRotateY = rotateY;
		return this;
	}

	/**
	 * Rotates the model around the Z-axis.
	 */
	public RenderParams rotateZ(double rotateZ) {
		mRotateZ = rotateZ;
		return this;
	}

	/**
	 * Offsets the model along the X-axis.
	 */
	public RenderParams offsetX(double offsetX) {
		mOffsetX = offsetX;
		return this;
	}

	/**
	 * Offsets the model along the Y-axis.
	 */
	public RenderParams offsetY(double offsetY) {
		mOffsetY = offsetY;
		return this;
	}

	/**
	 * Offsets the model along the Z-axis.
	 */
	public RenderParams offsetZ(double offsetZ) {
		mOffsetZ = offsetZ;
		return this;
	}
}