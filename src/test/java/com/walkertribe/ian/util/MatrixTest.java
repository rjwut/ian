package com.walkertribe.ian.util;

import org.junit.Assert;
import org.junit.Test;

public class MatrixTest {
	private static final Matrix POINT = new Matrix(1, 1, 1);
	private static final double[][] TEST_MATRIX = new double[][] {
		new double[] { 0 },
		new double[] { 1 }
	};
	private static final double[][] TEST_MATRIX_2 = new double[][] {
		new double[] { 0, 1 },
		new double[] { 2, 3 }
	};

	@Test
	public void testRotateX() {
		Matrix result = Matrix.buildRotateXMatrix(Math.PI / 2).multiply(POINT);
		Assert.assertEquals(new Matrix(1, -1, 1), result);
	}

	@Test
	public void testRotateY() {
		Matrix result = Matrix.buildRotateYMatrix(Math.PI / 2).multiply(POINT);
		Assert.assertEquals(new Matrix(1, 1, -1), result);
	}

	@Test
	public void testRotateZ() {
		Matrix result = Matrix.buildRotateZMatrix(Math.PI / 2).multiply(POINT);
		Assert.assertEquals(new Matrix(-1, 1, 1), result);
	}

	@Test
	public void testScale() {
		Matrix result = Matrix.buildScaleMatrix(2).multiply(POINT);
		Assert.assertEquals(new Matrix(2, 2, 2), result);
	}

	@Test
	public void testSizeConstructor() {
		Matrix result = new Matrix(1, 2);
		Assert.assertEquals(1, result.getRowCount());
		Assert.assertEquals(2, result.getColumnCount());
		Assert.assertEquals(0, result.get(0, 0), TestUtil.EPSILON);
		Assert.assertEquals(0, result.get(0, 1), TestUtil.EPSILON);
		result.set(0, 0, 1.0);
		Assert.assertEquals(1.0, result.get(0, 0), TestUtil.EPSILON);
	}

	@Test
	public void test2dArrayConstructor() {
		Matrix result = new Matrix(TEST_MATRIX);
		Assert.assertEquals(2, result.getRowCount());
		Assert.assertEquals(1, result.getColumnCount());
		Assert.assertEquals(0, result.get(0, 0), TestUtil.EPSILON);
		Assert.assertEquals(1, result.get(1, 0), TestUtil.EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test2dArrayConstructorNoRows() {
		new Matrix(new double[][] {});
	}

	@Test(expected = IllegalArgumentException.class)
	public void test2dArrayNoColumns() {
		new Matrix(new double[][] {
			new double[] {}
		});
	}

	@Test(expected = IllegalArgumentException.class)
	public void test2dArrayUnevenRows() {
		new Matrix(new double[][] {
			new double[] { 0 },
			new double[] { 1, 2 }
		});
	}

	@Test
	public void testAddAndSubtract() {
		Assert.assertEquals(new Matrix(2, 2, 2), POINT.add(POINT));
		Assert.assertEquals(new Matrix(0, 0, 0), POINT.subtract(POINT));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddDifferentRowCount() {
		POINT.add(new Matrix(1, 1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddDifferentColumnCount() {
		POINT.add(new Matrix(3, 2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMultiplyMalformed() {
		POINT.multiply(POINT);
	}

	@Test
	public void testEqualsAndHashCode() {
		TestUtil.testEqualsAndHashCode(
				POINT,
				new Matrix(1, 1, 1),
				new Matrix(new double[][] {
					new double[] { 1, 1, 1 }
				}),
				new Matrix(new double[][] {
					new double[] { 1, 1 },
					new double[] { 1, 1 },
					new double[] { 1, 1 }
				}),
				new Matrix(1, 1, 1.1)
		);
	}

	@Test
	public void testHashCode() {
		Assert.assertTrue(POINT.hashCode() == POINT.hashCode());
		Assert.assertFalse(POINT.hashCode() == new Matrix(1, 1, 1.1).hashCode());
	}

	@Test
	public void testToString() {
		Assert.assertEquals("0.0 | 1.0\n2.0 | 3.0\n", new Matrix(TEST_MATRIX_2).toString());
	}

	@Test
	public void makeEclEmmaHappy() {
		TestUtil.coverEnumValueOf(Matrix.Op.class);
	}
}
