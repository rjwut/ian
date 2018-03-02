package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.world.ArtemisPlayer;
import com.walkertribe.ian.world.ArtemisPlayerTest;

public class UpgradesParserTest extends AbstractObjectUpdatePacketTester<ArtemisPlayer> {
	@Test
	public void test() {
		execute(UpgradesParser.class, 2);
	}

	@Override
	protected void testObjects(List<ArtemisPlayer> objects) {
		ArtemisPlayerTest.assertPopulatedUpgrades(objects.get(0));
		ArtemisPlayerTest.assertUnpopulatedUpgrades(objects.get(1));
	}
}
