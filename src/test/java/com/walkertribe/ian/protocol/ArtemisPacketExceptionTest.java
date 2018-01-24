package com.walkertribe.ian.protocol;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;

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
		ArtemisPacketException ex = new ArtemisPacketException("test", ConnectionType.SERVER);
		Assert.assertEquals(ConnectionType.SERVER, ex.getConnectionType());
		ex = new ArtemisPacketException(new RuntimeException(), ConnectionType.SERVER, 47);
		Assert.assertEquals(ConnectionType.SERVER, ex.getConnectionType());
		Assert.assertEquals(47, ex.getPacketType());
		ex = new ArtemisPacketException(new RuntimeException(), ConnectionType.SERVER, 47, new byte[] { 47 });
		Assert.assertEquals(ConnectionType.SERVER, ex.getConnectionType());
		Assert.assertEquals(47, ex.getPacketType());
		Assert.assertArrayEquals(new byte[] { 47 }, ex.getPayload());
	}
}
