package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.world.ArtemisPlayer;
import com.walkertribe.ian.world.ArtemisPlayerTest;

public class WeapParserTest extends AbstractObjectUpdatePacketTester<ArtemisPlayer> {
	@Test
	public void test() {
		execute(WeapParser.class, 2);
	}

	@Override
	protected void testObjects(List<ArtemisPlayer> objects) {
		ArtemisPlayerTest.assertPopulatedWeap(objects.get(0));
		ArtemisPlayerTest.assertUnpopulatedWeap(objects.get(1));
	}
}
