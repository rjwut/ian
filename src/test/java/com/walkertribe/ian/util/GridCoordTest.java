package com.walkertribe.ian.util;

import org.junit.Assert;
import org.junit.Test;

public class GridCoordTest {
	@Test
	public void testCoords() {
		GridCoord coord = GridCoord.get(0, 1, 2);
		Assert.assertEquals(0, coord.x());
		Assert.assertEquals(1, coord.y());
		Assert.assertEquals(2, coord.z());
	}

	@Test
	public void testEquals() {
		// GridCoords are immutable and have private constructors, so we can't
		// use TestUtil.testEqualsAndHashCode()
		GridCoord coord1 = GridCoord.get(0, 0, 0);
		GridCoord coord2 = GridCoord.get(0, 0, 1);
		Assert.assertEquals(coord1, coord1);
		Assert.assertNotEquals(coord1, null);
		Assert.assertNotEquals(coord1, "foo");
		Assert.assertNotEquals(coord1, coord2);
		Assert.assertTrue(coord1.at(0, 0, 0));
		Assert.assertFalse(coord1.at(0, 0, 1));
		Assert.assertEquals(coord1.hashCode(), coord1.hashCode());
	}

	@Test
	public void testToString() {
		Assert.assertEquals("[0,1,2]", GridCoord.get(0, 1, 2).toString());
	}

	@Test
	public void compareTo() {
		GridCoord coord1 = GridCoord.get(1, 0, 0);
		GridCoord coord2 = GridCoord.get(0, 1, 0);
		GridCoord coord3 = GridCoord.get(0, 0, 1);
		Assert.assertEquals(0, coord1.compareTo(coord1));
        Assert.assertEquals(0, coord2.compareTo(coord2));
        Assert.assertEquals(0, coord3.compareTo(coord3));
		Assert.assertTrue(coord1.compareTo(coord2) > 0);
		Assert.assertTrue(coord2.compareTo(coord3) > 0);
		Assert.assertTrue(coord1.compareTo(coord3) > 0);
	}
}
