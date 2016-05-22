package com.walkertribe.ian.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Creates and stores a top-down silhouette of a Model. As models can have many
 * polygons and can be expensive to render, using this class will typically be
 * more performant for rendering silhouettes.
 * @author rjwut
 */
public class Silhouette {
	private static final DefaultModelRenderer RENDERER = new DefaultModelRenderer();
	private int size;
	private int halfSize;
	private BufferedImage image;

	/**
	 * Create a new Silhouette of the given Model, rendered using the indicated
	 * size and color.
	 */
	public Silhouette(Model model, int size, Color color) {
		image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		this.size = size;
		halfSize = size / 2;
		RenderParams params = new RenderParams()
				.fillColor(color)
				.offsetX(halfSize)
				.offsetZ(halfSize)
				.renderMode(RenderMode.SOLID)
				.scale(model.computeScale(halfSize));
		RENDERER.render(image.createGraphics(), model, params);
	}

	/**
	 * Renders this Silhouette onto the given Graphics2D context, scaled to fit
	 * within a circle of the given radius.
	 */
	public void render(Graphics2D g, int x, int y, double radius,
			double heading, String label) {
		double scale = radius / halfSize;
		AffineTransform xform = new AffineTransform();
		xform.scale(scale, scale);
		xform.rotate(heading, halfSize, halfSize);
		int scaledSize = (int) Math.ceil(size * scale);
		int halfScaledSize = scaledSize / 2;
		BufferedImage temp = new BufferedImage(scaledSize, scaledSize, BufferedImage.TYPE_INT_ARGB);
		temp.createGraphics().drawImage(image, xform, null);
		g.drawImage(temp, x - halfScaledSize, y - halfScaledSize, null);

		if (label != null) {
			int labelX = x - (g.getFontMetrics().stringWidth(label) / 2);
			int labelY = y - halfScaledSize;
			g.drawString(label, labelX, labelY);
		}
	}
}