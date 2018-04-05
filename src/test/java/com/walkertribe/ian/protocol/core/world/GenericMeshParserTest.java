package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.world.ArtemisMesh;
import com.walkertribe.ian.world.ArtemisMeshTest;

public class GenericMeshParserTest extends AbstractObjectUpdatePacketTester<ArtemisMesh> {
	@Test
	public void test() {
		execute(GenericMeshParser.class, 1);
	}

	@Override
	protected void testObjects(List<ArtemisMesh> objects) {
		ArtemisMeshTest.assertMesh(objects.get(0), 2, "MESH", 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, "test.dxs",
				"test.png", 10f, BoolState.TRUE, 11f, 1f, 1f, 1f, 100, 50);
	}
}
