package com.walkertribe.ian.protocol;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.util.TextUtil;

public class UnknownPacketTest extends AbstractPacketTester<UnknownPacket> {
	private static final String PAYLOAD = "13579ace";

	@Test
	public void test() {
		execute("UnknownPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		test(new UnknownPacket(Origin.SERVER, 0xffffffff, TextUtil.hexToByteArray(PAYLOAD)));
	}

	@Override
	protected void testPackets(List<UnknownPacket> packets) {
		// do nothing
	}

	private void test(UnknownPacket pkt) {
		Assert.assertEquals(Origin.SERVER, pkt.getOrigin());
		Assert.assertEquals(0xffffffff, pkt.getType());
		Assert.assertEquals(PAYLOAD, TextUtil.byteArrayToHexString(pkt.getPayload()));
	}
}