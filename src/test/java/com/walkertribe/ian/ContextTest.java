package com.walkertribe.ian;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.FactionAttribute;
import com.walkertribe.ian.vesseldata.Faction;
import com.walkertribe.ian.vesseldata.VesselData;

public class ContextTest {
	@Test
	public void testVesselData() {
		Context ctx = new DefaultContext(new ClasspathResolver(VesselData.class));
		VesselData data = ctx.getVesselData();
		Assert.assertNotNull(data);
		Faction faction = data.getFaction(1);
		Assert.assertEquals(1, faction.getId());
		Assert.assertEquals("Terran", faction.getName());
		Assert.assertTrue(faction.is(FactionAttribute.FRIENDLY));
		ctx.getVesselData(); // exercise cache
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructContextNull() {
		new DefaultContext(null);
	}
}