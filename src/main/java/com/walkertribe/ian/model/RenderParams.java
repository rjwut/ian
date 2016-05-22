package com.walkertribe.ian.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

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
	RenderMode mMode = RenderMode.WIREFRAME;
	Color mLineColor = Color.WHITE;
	Color mFillColor = Color.BLACK;

	/**
	 * Scaling factor: a value greater than 1 will scale it up, and less
	 * than 1 will scale it down.
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

	/**
	 * Dictates the model render style.
	 */
	public RenderParams renderMode(RenderMode mode) {
		mMode = mode;
		return this;
	}

	/**
	 * Sets the render line color.
	 */
	public RenderParams lineColor(Color lineColor) {
		mLineColor = lineColor;
		return this;
	}

	/**
	 * Sets the render fill color.
	 */
	public RenderParams fillColor(Color fillColor) {
		mFillColor = fillColor;
		return this;
	}

	/**
	 * Renders the given Polygon.
	 */
	void renderPolygon(Graphics2D g, Polygon polygon) {
		mMode.renderPolygon(g, this, polygon);
	}
}