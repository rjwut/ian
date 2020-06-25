package com.walkertribe.ian.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.util.Matrix;
import com.walkertribe.ian.util.TestUtil;

public class PointTest {
	@Test
	public void test() {
		Point point = new Point(1.0, 2.0, 3.0);
		double[] coords = point.getCoordinates();
		Assert.assertEquals(1.0, coords[0], TestUtil.EPSILON);
		Assert.assertEquals(2.0, coords[1], TestUtil.EPSILON);
		Assert.assertEquals(-3.0, coords[2], TestUtil.EPSILON);
		Assert.assertEquals(3.7416573867739413, point.r(), TestUtil.EPSILON);
	}

	@Test
	public void testApplyMatrices() {
		Point point = new Point(1.0, 2.0, 3.0);
		List<Matrix> list = new ArrayList<Matrix>();
		list.add(Matrix.buildRotateXMatrix(Math.PI));
		double[] coords = point.applyMatrices(list);
		Assert.assertEquals(1.0, coords[0], TestUtil.EPSILON);
		Assert.assertEquals(-2.0, coords[1], TestUtil.EPSILON);
		Assert.assertEquals(3.0, coords[2], TestUtil.EPSILON);
	}
}
