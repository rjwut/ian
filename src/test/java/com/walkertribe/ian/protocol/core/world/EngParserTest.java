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
		ArtemisPlayerTest.assertEng(
				objects.get(0),
				ArtemisPlayerTest.ENG_FLOATS_UNSPECIFIED,
				ArtemisPlayerTest.ENG_FLOATS_UNSPECIFIED,
				ArtemisPlayerTest.ENG_BYTES_UNSPECIFIED
		);
		ArtemisPlayerTest.assertEng(
				objects.get(1),
				ArtemisPlayerTest.ENG_FLOATS,
				ArtemisPlayerTest.ENG_FLOATS,
				ArtemisPlayerTest.ENG_BYTES
		);
	}
}
