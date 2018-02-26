package com.walkertribe.ian.protocol.core.comm;

import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.walkertribe.ian.enums.AudioMode;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;

public class IncomingAudioPacketTest extends AbstractPacketTester<IncomingAudioPacket> {
	@Test
	public void testParse() {
		execute("core/comm/IncomingAudioPacket.txt", Origin.SERVER, 2);
	}

	@Test
	public void testConstruct() {
		new IncomingAudioPacket(1);
		new IncomingAudioPacket(1, "Title", "file");
	}

	@Override
	protected void testPackets(List<IncomingAudioPacket> packets) {
		IncomingAudioPacket pkt = packets.get(0);
		Assert.assertEquals(0, pkt.getAudioId());
		Assert.assertEquals(AudioMode.INCOMING, pkt.getAudioMode());
		TestUtil.assertToStringEquals("Hello", pkt.getTitle());
		TestUtil.assertToStringEquals("hello.ogg", pkt.getFileName());
		pkt = packets.get(1);
		Assert.assertEquals(1, pkt.getAudioId());
		Assert.assertEquals(AudioMode.PLAYING, pkt.getAudioMode());
		Assert.assertNull(pkt.getTitle());
		Assert.assertNull(pkt.getFileName());
	}
}