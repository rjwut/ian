package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisDroneTest {
	@Test
	public void testUpdateFrom() {
		ArtemisDrone obj0 = new ArtemisDrone(47);
		assertUnknownDrone(obj0, 47);
		ArtemisDrone obj1 = new ArtemisDrone(47);
		obj1.setName("DRONE");
		obj1.setX(1.0f);
		obj1.setY(2.0f);
		obj1.setZ(3.0f);
		obj1.setHeading(4.0f);
		assertDrone(obj1, 47, "DRONE", 1.0f, 2.0f, 3.0f, 4.0f);
		obj1.updateFrom(obj0);
		assertDrone(obj1, 47, "DRONE", 1.0f, 2.0f, 3.0f, 4.0f);
		obj0.updateFrom(obj1);
		assertDrone(obj0, 47, "DRONE", 1.0f, 2.0f, 3.0f, 4.0f);
		obj0.updateFrom(new ArtemisAnomaly(48));
	}

	public static void assertUnknownDrone(ArtemisDrone drone, int id) {
		ArtemisObjectTest.assertUnknownObject(drone, id, ObjectType.DRONE);
		ArtemisOrientableTest.assertUnknownOrientable(drone);
	}

	public static void assertDrone(ArtemisDrone drone, int id, String name, float x, float y, float z, float heading) {
		ArtemisObjectTest.assertObject(drone, id, ObjectType.DRONE, name, x, y, z);
		Assert.assertEquals(heading, drone.getHeading(), TestUtil.EPSILON);
	}
}
