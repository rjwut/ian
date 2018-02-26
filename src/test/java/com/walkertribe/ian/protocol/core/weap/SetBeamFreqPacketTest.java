package com.walkertribe.ian.protocol.core.weap;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class SetBeamFreqPacketTest extends AbstractPacketTester<SetBeamFreqPacket> {
	@Test
	public void test() {
		execute("core/weap/SetBeamFreqPacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		test(new SetBeamFreqPacket(BeamFrequency.B));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullBeamFrequency() {
		new SetBeamFreqPacket((BeamFrequency) null);
	}

	@Override
	protected void testPackets(List<SetBeamFreqPacket> packets) {
		test(packets.get(0));
	}

	private void test(SetBeamFreqPacket pkt) {
		Assert.assertEquals(BeamFrequency.B, pkt.getBeamFrequency());
	}
}
