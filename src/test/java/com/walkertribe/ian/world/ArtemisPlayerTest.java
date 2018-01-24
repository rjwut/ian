package com.walkertribe.ian.world;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.util.TestUtil;

public class ArtemisPlayerTest {
	public static final float[] ENG_FLOATS_UNSPECIFIED = new float[] { -1f, -1f, -1f, -1f, -1f, -1f, -1f, -1f };
	public static final byte[] ENG_BYTES_UNSPECIFIED = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1 };
	public static final float[] ENG_FLOATS = new float[] { 0f, 0.125f, 0.25f, 0.375f, 0.5f, 0.625f, 0.75f, 0.875f };
	public static final byte[] ENG_BYTES = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };

	@Test
	public void testUpdateFromEng() {
		ArtemisPlayer obj0 = new ArtemisPlayer(47);
		ArtemisPlayerTest.assertEng(obj0, ENG_FLOATS_UNSPECIFIED, ENG_FLOATS_UNSPECIFIED, ENG_BYTES_UNSPECIFIED);
		ArtemisPlayer obj1 = new ArtemisPlayer(47);

		for (ShipSystem sys : ShipSystem.values()) {
			int index = sys.ordinal();
			obj1.setSystemHeat(sys, ENG_FLOATS[index]);
			obj1.setSystemEnergyAsPercent(sys, ENG_FLOATS[index] * Artemis.MAX_ENERGY_ALLOCATION_PERCENT);
			obj1.setSystemCoolant(sys, ENG_BYTES[index]);
		}

		ArtemisPlayerTest.assertEng(obj1, ENG_FLOATS, ENG_FLOATS, ENG_BYTES);
		obj1.updateFrom(obj0, null);
		ArtemisPlayerTest.assertEng(obj1, ENG_FLOATS, ENG_FLOATS, ENG_BYTES);
		obj0.updateFrom(obj1, null);
		ArtemisPlayerTest.assertEng(obj0, ENG_FLOATS, ENG_FLOATS, ENG_BYTES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngHeatTooLow() {
		new ArtemisPlayer(47).setSystemHeat(ShipSystem.BEAMS, -0.0001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngHeatTooHigh() {
		new ArtemisPlayer(47).setSystemHeat(ShipSystem.BEAMS, 1.0001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngEnergyTooLow() {
		new ArtemisPlayer(47).setSystemEnergy(ShipSystem.BEAMS, -0.0001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngEnergyTooHigh() {
		new ArtemisPlayer(47).setSystemEnergy(ShipSystem.BEAMS, 1.0001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngCoolantTooLow() {
		new ArtemisPlayer(47).setSystemCoolant(ShipSystem.BEAMS, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEngCoolantTooHigh() {
		new ArtemisPlayer(47).setSystemCoolant(ShipSystem.BEAMS, Artemis.MAX_COOLANT_PER_SYSTEM + 1);
	}

	public static void assertEng(ArtemisPlayer player, float[] heat, float[] energy, byte[] coolant) {
		for (ShipSystem system : ShipSystem.values()) {
			int index = system.ordinal();
			Assert.assertEquals(heat[index], player.getSystemHeat(system), TestUtil.EPSILON);
			Assert.assertEquals(energy[index], player.getSystemEnergy(system), TestUtil.EPSILON);
			Assert.assertEquals(coolant[index], player.getSystemCoolant(system));
		}
	}
}
