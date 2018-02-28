package com.walkertribe.ian.protocol;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;

public class ArtemisPacketExceptionTest {
	// Done separately to make SpotBugs happy.
	@Test(expected = ArtemisPacketException.class)
	public void testConstructMessage() throws ArtemisPacketException {
		throw new ArtemisPacketException("test");
	}

	// Done separately to make SpotBugs happy.
	@Test(expected = ArtemisPacketException.class)
	public void testConstructCause() throws ArtemisPacketException {
		throw new ArtemisPacketException(new RuntimeException());
	}

	@Test
	public void testRest() {
		ArtemisPacketException ex = new ArtemisPacketException("test", Origin.SERVER);
		Assert.assertEquals(Origin.SERVER, ex.getOrigin());
		ex = new ArtemisPacketException(new RuntimeException(), Origin.SERVER, 47);
		Assert.assertEquals(Origin.SERVER, ex.getOrigin());
		Assert.assertEquals(47, ex.getPacketType());
		ex = new ArtemisPacketException(new RuntimeException(), Origin.SERVER, 47, new byte[] { 47 });
		Assert.assertEquals(Origin.SERVER, ex.getOrigin());
		Assert.assertEquals(47, ex.getPacketType());
		Assert.assertArrayEquals(new byte[] { 47 }, ex.getPayload());
	}
}
