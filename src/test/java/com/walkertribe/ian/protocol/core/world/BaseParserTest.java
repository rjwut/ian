package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.world.ArtemisBase;
import com.walkertribe.ian.world.ArtemisBaseTest;

public class BaseParserTest extends AbstractObjectUpdatePacketTester<ArtemisBase> {
	@Test
	public void test() {
		execute(BaseParser.class, 1);
	}

	@Override
	protected void testObjects(List<ArtemisBase> objects) {
		ArtemisBaseTest.assertBase(objects.get(0), 2, "DS1", 47.0f, 80.0f, 1000, 1.0f, 2.0f, 3.0f);
	}
}
