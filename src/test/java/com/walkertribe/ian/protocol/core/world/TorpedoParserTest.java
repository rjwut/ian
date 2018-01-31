package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.world.ArtemisGenericObject;
import com.walkertribe.ian.world.ArtemisObjectTest;

public class TorpedoParserTest extends AbstractObjectUpdatePacketTester<ArtemisGenericObject> {
	@Test
	public void test() {
		execute(TorpedoParser.class, 1);
	}

	@Override
	protected void testObjects(List<ArtemisGenericObject> objects) {
		ArtemisObjectTest.assertObject(objects.get(0), 2, ObjectType.TORPEDO, null, 1.0f, 2.0f, 3.0f);
	}
}
