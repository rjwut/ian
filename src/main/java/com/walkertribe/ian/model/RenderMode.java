package com.walkertribe.ian.model;

import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 * Used in by RenderParams to indicate the desired render mode for
 * DefaultModelRenderer.
 * @author rjwut
 */
public enum RenderMode {
	WIREFRAME {
		@Override
		public void renderPolygon(Graphics2D g, RenderParams config, Polygon polygon) {
			g.setColor(config.mLineColor);
			g.draw(polygon);
		}
	},
	SOLID {
		@Override
		public void renderPolygon(Graphics2D g, RenderParams config, Polygon polygon) {
			g.setColor(config.mFillColor);
			g.fill(polygon);
		}
	},
	SOLID_WIREFRAME {
		@Override
		public void renderPolygon(Graphics2D g, RenderParams config,
				Polygon polygon) {
			SOLID.renderPolygon(g, config, polygon);
			WIREFRAME.renderPolygon(g, config, polygon);
		}

		@Override
		public boolean polysMustBeSorted() {
			return true;
		}
	};

	/**
	 * Renders the given Polygon in this style.
	 */
	public abstract void renderPolygon(Graphics2D g, RenderParams config,
			Polygon polygon);

	/**
	 * Returns true if this RenderMode requires that polygons be rendered back
	 * to front; false otherwise.
	 */
	public boolean polysMustBeSorted() {
		return false;
	}
}