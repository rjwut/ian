package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.world.ArtemisPlayer;
import com.walkertribe.ian.world.ArtemisPlayerTest;

public class EngParserTest extends AbstractObjectUpdatePacketTester<ArtemisPlayer> {
	@Test
	public void test() {
		execute(EngParser.class, 2);
	}

	@Override
	protected void testObjects(List<ArtemisPlayer> objects) {
		ArtemisPlayerTest.assertUnpopulatedEng(objects.get(0));
		ArtemisPlayerTest.assertEng(
				objects.get(1),
				ArtemisPlayerTest.ENG_FLOATS,
				ArtemisPlayerTest.ENG_FLOATS,
				ArtemisPlayerTest.ENG_BYTES
		);
	}
}
