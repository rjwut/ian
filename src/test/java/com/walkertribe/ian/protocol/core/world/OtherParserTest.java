package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.world.ArtemisGenericObject;
import com.walkertribe.ian.world.ArtemisObjectTest;

public class OtherParserTest extends AbstractObjectUpdatePacketTester<ArtemisGenericObject> {
	@Test
	public void test() {
		execute(OtherParser.class, 1);
	}

	@Override
	protected void testObjects(List<ArtemisGenericObject> objects) {
		ArtemisObjectTest.assertObject(objects.get(0), 2, ObjectType.MINE, null, 1.0f, 2.0f, 3.0f);
	}
}
