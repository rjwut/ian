package com.walkertribe.ian.model;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.walkertribe.ian.vesseldata.Vessel;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * A default ModelRenderer implementation. This class renders models as
 * wireframes or silhouettes in orthographic projection.
 * @author rjwut
 */
public class DefaultModelRenderer implements ModelRenderer<RenderParams> {
	@Override
	public void render(Graphics2D g, ArtemisObject object,
			RenderParams params) {
		Model model = object.getModel();

		if (model != null) {
			render(g, model, params);
		}
	}

	@Override
	public void render(Graphics2D g, Vessel vessel, RenderParams params) {
		render(g, vessel.getModel(), params);
	}

	@Override
	public void render(Graphics2D g, Model model, RenderParams params) {
		final Map<String, double[]> pointMap = model.transformVertices(params);
		List<Poly> sortedPolys = model.getPolys();

		if (params.mMode.polysMustBeSorted()) {
			Collections.sort(sortedPolys, new Comparator<Poly>() {
				@Override
				public int compare(Poly o1, Poly o2) {
					double y1 = findMinY(o1, pointMap);
					double y2 = findMinY(o2, pointMap);
					return (int) Math.signum(y1 - y2);
				}
			});
		}

		for (Poly poly : sortedPolys) {
			params.renderPolygon(g, poly.toPolygon(pointMap, params));
		}
	}

	/**
	 * Finds the smallest Y value for the given Poly.
	 */
	private static double findMinY(Poly poly, Map<String, double[]> pointMap) {
		double minY = 0;

		for (int i = 0; i < poly.vertexIds.length; i++) {
			double y = pointMap.get(poly.vertexIds[i])[1];
			minY = i == 0 ? y : Math.min(minY, y);
		}

		return minY;
	}
}