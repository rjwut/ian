package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.world.ArtemisCreature;
import com.walkertribe.ian.world.ArtemisCreatureTest;

public class CreatureParserTest extends AbstractObjectUpdatePacketTester<ArtemisCreature> {
	@Test
	public void test() {
		execute(CreatureParser.class, 2);
	}

	@Override
	protected void testObjects(List<ArtemisCreature> objects) {
		ArtemisCreatureTest.assertCreature(objects.get(0), 2, 1.0f, 2.0f, 3.0f, "WHALE", 4.0f, 5.0f, 6.0f,
				CreatureType.WHALE, 50, 100);
		ArtemisCreatureTest.assertCreature(objects.get(1), 2, 1.0f, 2.0f, 3.0f, "WHALE", 4.0f, 5.0f, 6.0f, null, 50,
				100);
	}
}
