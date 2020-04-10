package com.walkertribe.ian.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.util.TestUtil;

public class ModelTest {
	@Test
	public void testConstruct() {
		Model model = buildModel();
		Assert.assertEquals(0.2672612419124244, model.computeScale(1.0), TestUtil.EPSILON);
		Assert.assertEquals("test", model.getDxsPaths());
		Assert.assertEquals(3.741657386773941, model.getSize(), TestUtil.EPSILON);
	}

	@Test
	public void testTransform() {
		Model model = buildModel();
		RenderParams params = new RenderParams();
		params.rotateX(Math.PI);
		params.rotateY(Math.PI);
		params.rotateZ(Math.PI);
		params.offsetX(1.0);
		params.offsetY(2.0);
		params.offsetZ(3.0);
		params.scale(2.0);
		Map<String, double[]> map = model.transformPoints(params);
		double[] coords = map.get("1");
		Assert.assertArrayEquals(new double[] { 3.0, 6.0, -3.0 }, coords, TestUtil.EPSILON);
		coords = map.get("2");
		Assert.assertArrayEquals(new double[] { 5.0, 8.0, 1.0 }, coords, TestUtil.EPSILON);
		coords = map.get("3");
		Assert.assertArrayEquals(new double[] { 7.0, 4.0, -1.0 }, coords, TestUtil.EPSILON);
	}

	@Test
	public void testIdentityTransform() {
		Model model = buildModel();
		RenderParams params = new RenderParams();
		Map<String, double[]> map = model.transformPoints(params);
		double[] coords = map.get("1");
		Assert.assertArrayEquals(new double[] { 1.0, 2.0, -3.0 }, coords, TestUtil.EPSILON);
		coords = map.get("2");
		Assert.assertArrayEquals(new double[] { 2.0, 3.0, -1.0 }, coords, TestUtil.EPSILON);
		coords = map.get("3");
		Assert.assertArrayEquals(new double[] { 3.0, 1.0, -2.0 }, coords, TestUtil.EPSILON);
	}

	@Test
	public void testEqualsAndHashCode() {
		TestUtil.testEqualsAndHashCode(
				new Model("test"),
				new Model("test"),
				new Model("foo")
		);
	}

	private Model buildModel() {
		Map<String, Point> vertices = new TreeMap<String, Point>();
		vertices.put("1", new Point(1.0, 2.0, 3.0));
		vertices.put("2", new Point(2.0, 3.0, 1.0));
		vertices.put("3", new Point(3.0, 1.0, 2.0));
		Poly poly = new Poly(new ArrayList<String>(vertices.keySet()));
		List<Poly> polys = new ArrayList<Poly>();
		polys.add(poly);
		Model model = new Model("test");
		model.add(vertices, polys);
		return model;
	}
}
