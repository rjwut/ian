package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.Upgrade;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class ActivateUpgradePacketTest extends AbstractPacketTester<ActivateUpgradePacket> {
	@Test
	public void test() {
		execute("core/ActivateUpgradePacket.txt", Origin.CLIENT, 1);
	}

	@Test
	public void testConstruct() {
		Assert.assertEquals(Upgrade.CARPACTION_COILS, new ActivateUpgradePacket(Upgrade.CARPACTION_COILS).getUpgrade());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNull() {
		new ActivateUpgradePacket((Upgrade) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidUpgrade() {
		new ActivateUpgradePacket(Upgrade.TACHYON_SCANNERS);
	}

	@Override
	protected void testPackets(List<ActivateUpgradePacket> packets) {
		Assert.assertEquals(Upgrade.CARPACTION_COILS, packets.get(0).getUpgrade());
	}

}
