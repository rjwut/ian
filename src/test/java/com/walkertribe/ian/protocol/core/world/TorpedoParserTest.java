package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.enums.OrdnanceType;
import com.walkertribe.ian.world.ArtemisTorpedo;
import com.walkertribe.ian.world.ArtemisTorpedoTest;

public class TorpedoParserTest extends AbstractObjectUpdatePacketTester<ArtemisTorpedo> {
	@Test
	public void test() {
		execute(TorpedoParser.class, 2);
	}

	@Override
	protected void testObjects(List<ArtemisTorpedo> objects) {
		ArtemisTorpedoTest.assertTorpedo(objects.get(0), 2, 1f, 2f, 3f, 4f, 5f, 6f, OrdnanceType.EMP);
		ArtemisTorpedoTest.assertUnpopulatedTorpedo(objects.get(1), 2);
	}
}
