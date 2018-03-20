package com.walkertribe.ian.protocol;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.BaseDebugger;
import com.walkertribe.ian.iface.Debugger;
import com.walkertribe.ian.iface.OutputStreamDebugger;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.JamCrc;
import com.walkertribe.ian.util.TestUtil;
import com.walkertribe.ian.util.TextUtil;

public class UnparsedPacketTest extends AbstractPacketTester<UnknownPacket> {
	private static final String PAYLOAD = "0900000002000000";

	@Test
	public void test() {
		execute("core/SkyboxPacket.txt", Origin.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		test(new UnparsedPacket(Origin.SERVER, 0xf754c8fe, TextUtil.hexToByteArray(PAYLOAD)));
	}

	@Override
	protected void testPackets(List<UnknownPacket> packets) {
		// do nothing
	}

	private void test(UnparsedPacket pkt) {
		Assert.assertEquals(Origin.SERVER, pkt.getOrigin());
		Assert.assertEquals(JamCrc.compute(CorePacketType.SIMPLE_EVENT), pkt.getType());
		Assert.assertEquals(PAYLOAD, TextUtil.byteArrayToHexString(pkt.getPayload()));
	}

	/**
	 * Capture the UnparsedPacket via the debugger and test it.
	 */
	@Override
	protected Debugger getDebugger() {
		return TestUtil.DEBUG ? new OutputStreamDebugger("", System.out, System.err) {
			@Override
			public void onRecvUnparsedPacket(RawPacket pkt) {
				super.onRecvUnparsedPacket(pkt);
				test((UnparsedPacket) pkt);
			}
		} : new BaseDebugger() {
			@Override
			public void onRecvUnparsedPacket(RawPacket pkt) {
				test((UnparsedPacket) pkt);
			}
		};
	}

	/**
	 * Provide a PacketReader that won't parse the packet so that an
	 * UnparsedPacket will be produced.
	 */
	@Override
	protected PacketReader buildPacketReader(TestPacketFile file, Origin type) {
		return file.toPacketReader(type, false);
	}
}