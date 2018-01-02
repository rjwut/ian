package com.walkertribe.ian.util;

import java.util.Arrays;

/**
 * Represents a matrix and permits mathematical operations on them.
 * @author rjwut
 */
public class Matrix {
	private static final double EPSILON = 2.220446049250313E-16;

	/**
	 * Operations that can be performed in matrix cells. Used to build Matrix
	 * objects to perform various tranformations.
	 */
	enum Op {
		ZERO {
			@Override
			double apply(double value) {
				return 0;
			}
		},
		ONE {
			@Override
			double apply(double value) {
				return 1;
			}
		},
		VALUE {
			@Override
			double apply(double value) {
				return value;
			}
		},
		SIN_THETA {
			@Override
			double apply(double value) {
				return Math.sin(value);
			}
		},
		NEG_SIN_THETA {
			@Override
			double apply(double value) {
				return -Math.sin(value);
			}
		},
		COS_THETA {
			@Override
			double apply(double value) {
				return Math.cos(value);
			}
		};

		/**
		 * Computes the resulting value of a Matrix cell using this Op with the
		 * given input value.
		 */
		abstract double apply(double value);
	}

	private static final Op[][] SCALE = {
			{ Op.VALUE, Op.ZERO,  Op.ZERO  },
			{ Op.ZERO,  Op.VALUE, Op.ZERO  },
			{ Op.ZERO,  Op.ZERO,  Op.VALUE }
	};
	private static final Op[][] ROTATE_X = {
			{ Op.ONE,  Op.ZERO,      Op.ZERO    },
			{ Op.ZERO, Op.COS_THETA, Op.NEG_SIN_THETA },
			{ Op.ZERO, Op.SIN_THETA, Op.COS_THETA     }
	};
	private static final Op[][] ROTATE_Y = {
			{ Op.COS_THETA,     Op.ZERO, Op.SIN_THETA  },
			{ Op.ZERO,          Op.ONE,  Op.ZERO },
			{ Op.NEG_SIN_THETA, Op.ZERO, Op.COS_THETA  }
	};
	private static final Op[][] ROTATE_Z = {
			{ Op.COS_THETA, Op.NEG_SIN_THETA, Op.ZERO },
			{ Op.SIN_THETA, Op.COS_THETA,     Op.ZERO },
			{ Op.ZERO,      Op.ZERO,          Op.ONE  }
	};

	/**
	 * Returns a Matrix which will scale a Model by the given scale factor. For
	 * example, a value of 2 will produce a Matrix that can scale a Model such
	 * that each point is twice as far from the origin.
	 */
	public static Matrix buildScaleMatrix(double value) {
		return buildMatrix(SCALE, value);
	}

	/**
	 * Returns a Matrix which will rotate a Model about the X-axis by the given
	 * angle.
	 */
	public static Matrix buildRotateXMatrix(double theta) {
		return buildMatrix(ROTATE_X, theta);
	}

	/**
	 * Returns a Matrix which will rotate a Model about the Y-axis by the given
	 * angle.
	 */
	public static Matrix buildRotateYMatrix(double theta) {
		return buildMatrix(ROTATE_Y, theta);
	}

	/**
	 * Returns a Matrix which will rotate a Model about the Z-axis by the given
	 * angle.
	 */
	public static Matrix buildRotateZMatrix(double theta) {
		return buildMatrix(ROTATE_Z, theta);
	}

	/**
	 * Given an Op array defining a Matrix to build and an input value, returns
	 * the resulting Matrix.
	 */
	private static Matrix buildMatrix(Op[][] opArray, double value) {
		double[][] matrix = new double[3][];

		for (int r = 0; r < 3; r++) {
			Op[] rotRow = opArray[r];
			double[] row = new double[3];
			matrix[r] = row;

			for (int c = 0; c < 3; c++) {
				Op op = rotRow[c];
				row[c] = op.apply(value);
			}
		}

		return new Matrix(matrix, false);
	}

	private double[][] matrix;
	private Integer hashCode;

	/**
	 * Constructs a Matrix of the given dimensions, with all its cells set to 0.
	 */
	public Matrix(int rowCount, int columnCount) {
		matrix = new double[rowCount][];

		for (int r = 0; r < rowCount; r++) {
			matrix[r] = new double[columnCount];
		}
	}

	/**
	 * Constructs a Matrix with three rows and one column, representing the
	 * given 3D Cartesian coordinates.
	 */
	public Matrix(double x, double y, double z) {
		this(new double[][] { { x }, { y }, { z } }, false);
	}

	/**
	 * Constructs a Matrix populated with the given double values.
	 */
	public Matrix(double[][] matrix) {
		this(matrix, true);
	}

	/**
	 * Constructs a Matrix with the given double values. If safe is true, the
	 * array will be checked to ensure that it describes a valid matrix, and an
	 * IllegalArgumentException will be thrown if not. Otherwise, it will be
	 * assumed that the given array describes a valid matrix. This is used
	 * internally by this class to skip the sanity checks when the array in
	 * question is already known to be valid.
	 */
	private Matrix(double[][] matrix, boolean safe) {
		if (safe) {
			// sanity check input
			if (matrix.length == 0) {
				throw new IllegalArgumentException("Can't have a matrix with no rows");
			}

			int colCount = -1;

			for (int r = 0; r < matrix.length; r++) {
				double[] row = matrix[r];

				if (r == 0) {
					colCount = row.length;

					if (colCount == 0) {
						throw new IllegalArgumentException("Can't have a matrix with no columns");
					}
				} else {
					if (row.length != colCount) {
						throw new IllegalArgumentException("All rows must have the same number of cells");
					}
				}
			}
		}

		this.matrix = matrix;
	}

	/**
	 * Returns the number of rows in this Matrix.
	 */
	public int getRowCount() {
		return matrix.length;
	}

	/**
	 * Returns the number of columns in this Matrix.
	 */
	public int getColumnCount() {
		return matrix[0].length;
	}

	/**
	 * Returns the value located in the indicated cell.
	 */
	public double get(int r, int c) {
		return matrix[r][c];
	}

	/**
	 * Sets the value for the indicated cell.
	 */
	public void set(int r, int c, double value) {
		matrix[r][c] = value;
		hashCode = null;
	}

	/**
	 * Adds this Matrix with the given Matrix and returns the resulting Matrix.
	 * Note that the two Matrix objects must have the same dimensions.
	 */
	public Matrix add(Matrix other) {
		return addInternal(other, 1);
	}

	/**
	 * Subtracts the given Matrix from this Matrix and returns the resulting
	 * Matrix. Note that the two Matrix objects must have the same dimensions.
	 */
	public Matrix subtract(Matrix other) {
		return addInternal(other, -1);
	}

	/**
	 * Common implementation for both add() and subtract().
	 */
	private Matrix addInternal(Matrix other, double multiplier) {
		if (matrix.length != other.matrix.length) {
			throw new IllegalArgumentException("Matrices must have the same number of rows");
		}

		int columnCount = matrix[0].length;

		if (columnCount != other.matrix[0].length) {
			throw new IllegalArgumentException("Matrices must have the same number of columns");
		}

		double[][] result = new double[matrix.length][];

		for (int r = 0; r < matrix.length; r++) {
			double[] row1 = matrix[r];
			double[] row2 = other.matrix[r];
			double[] resultRow = new double[columnCount];
			result[r] = resultRow;

			for (int c = 0; c < columnCount; c++) {
				resultRow[c] = row1[c] + multiplier * row2[c];
			}
		}

		return new Matrix(result, false);
	}

	/**
	 * Multiplies this Matrix with the given Matrix and returns the resulting
	 * Matrix. Note that the number of columns in this Matrix and the number of
	 * rows in the other Matrix must be equal.
	 */
	public Matrix multiply(Matrix other) {
		int valueCount = getColumnCount();

		if (valueCount != other.getRowCount()) {
			throw new IllegalArgumentException("Other matrix must have the same number of rows as this matrix has columns");
		}

		int resultRows = getRowCount();
		int resultColumns = other.getColumnCount();
		double[][] result = new double[resultRows][];

		for (int r = 0; r < resultRows; r++) {
			double[] thisRow = matrix[r];
			double[] resultRow = new double[resultColumns];
			result[r] = resultRow;

			for (int c = 0; c < resultColumns; c++) {
				double sum = 0; 

				for (int v = 0; v < valueCount; v++) {
					sum += thisRow[v] * other.matrix[v][c];
				}

				resultRow[c] = sum;
			}
		}

		return new Matrix(result, false);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Matrix)) {
			return false;
		}

		Matrix that = (Matrix) obj;

		if (matrix.length != that.matrix.length) {
			return false;
		}

		if (matrix[0].length != that.matrix[0].length) {
			return false;
		}

		for (int r = 0; r < matrix.length; r++) {
			double[] thisRow = matrix[r];
			double[] thatRow = that.matrix[r];

			for (int c = 0; c < thisRow.length; c++) {
				if (Math.abs(thisRow[c] - thatRow[c]) >= EPSILON) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		if (hashCode == null) {
			Integer[] hashCodes = new Integer[matrix.length];

			for (int r = 0; r < matrix.length; r++) {
				hashCodes[r] = Arrays.hashCode(matrix[r]);
			}

			hashCode = Arrays.hashCode(hashCodes);
		}

		return hashCode.intValue();
	}
	
	/**
	 * Returns a String showing the contents of this Matrix.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();

		for (int r = 0; r < matrix.length; r++) {
			double[] row = matrix[r];

			for (int c = 0; c < row.length; c++) {
				if (c != 0) {
					b.append(" | ");
				}

				b.append(row[c]);
			}

			b.append('\n');
		}

		return b.toString();
	}
}