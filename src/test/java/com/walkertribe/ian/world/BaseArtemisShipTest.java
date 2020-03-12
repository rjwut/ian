package com.walkertribe.ian.world;

import org.junit.Assert;

import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.util.TestUtil;

public class BaseArtemisShipTest {
	public static void assertUnknownShip(BaseArtemisShip ship) {
		Assert.assertTrue(Float.isNaN(ship.getVelocity()));
		Assert.assertTrue(Float.isNaN(ship.getShieldsFrontMax()));
		Assert.assertTrue(Float.isNaN(ship.getShieldsRearMax()));

		for (BeamFrequency freq : BeamFrequency.values()) {
			Assert.assertTrue(Float.isNaN(ship.getShieldFreq(freq)));
		}

		Assert.assertTrue(Float.isNaN(ship.getSteering()));
		Assert.assertTrue(Float.isNaN(ship.getTopSpeed()));
		Assert.assertTrue(Float.isNaN(ship.getTurnRate()));
		Assert.assertTrue(Float.isNaN(ship.getImpulse()));
	}

	public static void assertShip(BaseArtemisShip ship, float velocity, float[] shieldFreqs,
	        float steering, float topSpeed, float turnRate, float impulse) {
		Assert.assertEquals(velocity, ship.getVelocity(), TestUtil.EPSILON);

		for (BeamFrequency freq : BeamFrequency.values()) {
			Assert.assertEquals(shieldFreqs[freq.ordinal()], ship.getShieldFreq(freq), TestUtil.EPSILON);
		}

		Assert.assertEquals(steering, ship.getSteering(), TestUtil.EPSILON);
		Assert.assertEquals(topSpeed, ship.getTopSpeed(), TestUtil.EPSILON);
		Assert.assertEquals(turnRate, ship.getTurnRate(), TestUtil.EPSILON);
		Assert.assertEquals(impulse, ship.getImpulse(), TestUtil.EPSILON);
	}
}
