package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.world.ArtemisDrone;
import com.walkertribe.ian.world.ArtemisDroneTest;

public class DroneParserTest extends AbstractObjectUpdatePacketTester<ArtemisDrone> {
	@Test
	public void test() {
		execute(DroneParser.class, 1);
	}

	@Override
	protected void testObjects(List<ArtemisDrone> objects) {
		ArtemisDroneTest.assertDrone(objects.get(0), 2, null, 1.0f, 2.0f, 3.0f, 4.0f);
	}

}
