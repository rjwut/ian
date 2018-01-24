package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisDroneTest {
	@Test
	public void testUpdateFrom() {
		ArtemisDrone obj0 = new ArtemisDrone(47);
		assertDrone(obj0, 47, null, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE,
				Float.MIN_VALUE, -1);
		ArtemisDrone obj1 = new ArtemisDrone(47);
		obj1.setName("DRONE");
		obj1.setX(1.0f);
		obj1.setY(2.0f);
		obj1.setZ(3.0f);
		obj1.setHeading(4.0f);
		obj1.setPitch(5.0f);
		obj1.setRoll(6.0f);
		obj1.setSteering(7.0f);
		assertDrone(obj1, 47, "DRONE", 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
		obj1.updateFrom(obj0, null);
		assertDrone(obj1, 47, "DRONE", 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
		obj0.updateFrom(obj1, null);
		assertDrone(obj0, 47, "DRONE", 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
		obj0.updateFrom(new ArtemisAnomaly(48), null);
	}

	public static void assertDrone(ArtemisDrone drone, int id, String name, float x, float y, float z, float heading,
			float pitch, float roll, float steering) {
		ArtemisObjectTest.assertObject(drone, id, ObjectType.DRONE, name, x, y, z);
		ArtemisOrientableTest.assertOrientable(drone, heading, pitch, roll);
		Assert.assertEquals(steering, drone.getSteering(), TestUtil.EPSILON);
	}
}
