package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.world.ArtemisNebula;
import com.walkertribe.ian.world.ArtemisNebulaTest;

public class NebulaParserTest extends AbstractObjectUpdatePacketTester<ArtemisNebula> {
	@Test
	public void test() {
		execute(NebulaParser.class, 1);
	}

	@Override
	protected void testObjects(List<ArtemisNebula> objects) {
		ArtemisNebulaTest.assertNebula(objects.get(0), 2, null, 1f, 2f, 3f, 1f, 1f, 1f);
	}

}
