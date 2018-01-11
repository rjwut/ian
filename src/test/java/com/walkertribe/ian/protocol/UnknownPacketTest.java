package com.walkertribe.ian.protocol;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.util.TextUtil;

public class UnknownPacketTest extends AbstractPacketTester<UnknownPacket> {
	private static final String PAYLOAD = "13579ace";

	@Test
	public void test() {
		execute("UnknownPacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		test(new UnknownPacket(ConnectionType.SERVER, 0xffffffff, TextUtil.hexToByteArray(PAYLOAD)));
	}

	@Override
	protected void testPackets(List<UnknownPacket> packets) {
		// do nothing
	}

	private void test(UnknownPacket pkt) {
		Assert.assertEquals(ConnectionType.SERVER, pkt.getConnectionType());
		Assert.assertEquals(0xffffffff, pkt.getType());
		Assert.assertEquals(PAYLOAD, TextUtil.byteArrayToHexString(pkt.getPayload()));
	}
}