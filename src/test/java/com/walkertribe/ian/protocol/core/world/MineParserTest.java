package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.world.ArtemisMine;
import com.walkertribe.ian.world.ArtemisObjectTest;

public class MineParserTest extends AbstractObjectUpdatePacketTester<ArtemisMine> {
	@Test
	public void test() {
		execute(MineParser.class, 1);
	}

	@Override
	protected void testObjects(List<ArtemisMine> objects) {
		ArtemisObjectTest.assertObject(objects.get(0), 2, ObjectType.MINE, null, 1.0f, 2.0f, 3.0f);
	}
}
