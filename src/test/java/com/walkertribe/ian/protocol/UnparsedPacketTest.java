package com.walkertribe.ian.protocol;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.BaseDebugger;
import com.walkertribe.ian.iface.Debugger;
import com.walkertribe.ian.iface.OutputStreamDebugger;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.TestUtil;
import com.walkertribe.ian.util.TextUtil;

public class UnparsedPacketTest extends AbstractPacketTester<UnknownPacket> {
	private static final String PAYLOAD = "0900000002000000";

	@Test
	public void test() {
		execute("core/SkyboxPacket.txt", ConnectionType.SERVER, 1);
	}

	@Test
	public void testConstruct() {
		test(new UnparsedPacket(ConnectionType.SERVER, 0xf754c8fe, TextUtil.hexToByteArray(PAYLOAD)));
	}

	@Override
	protected void testPackets(List<UnknownPacket> packets) {
		// do nothing
	}

	private void test(UnparsedPacket pkt) {
		Assert.assertEquals(ConnectionType.SERVER, pkt.getConnectionType());
		Assert.assertEquals(CorePacketType.SIMPLE_EVENT.getHash(), pkt.getType());
		Assert.assertEquals(PAYLOAD, TextUtil.byteArrayToHexString(pkt.getPayload()));
	}

	/**
	 * Capture the UnparsedPacket via the debugger and test it.
	 */
	@Override
	protected Debugger buildDebugger() {
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
	protected PacketReader buildPacketReader(TestPacketFile file, ConnectionType type) {
		return file.toPacketReader(type, false);
	}
}