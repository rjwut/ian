package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.world.ArtemisPlayer;
import com.walkertribe.ian.world.ArtemisPlayerTest;

public class PlayerShipParserTest extends AbstractObjectUpdatePacketTester<ArtemisPlayer> {
	@Test
	public void test() {
		execute(PlayerShipParser.class, 2);
	}

	@Override
	protected void testObjects(List<ArtemisPlayer> objects) {
		ArtemisPlayerTest.assertPopulatedPlayer(objects.get(0));
		ArtemisPlayerTest.assertUnpopulatedPlayer(objects.get(1));
	}
}
