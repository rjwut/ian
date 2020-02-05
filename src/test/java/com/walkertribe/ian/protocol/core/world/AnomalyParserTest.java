package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.AnomalyType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.world.ArtemisAnomaly;
import com.walkertribe.ian.world.ArtemisAnomalyTest;

public class AnomalyParserTest extends AbstractObjectUpdatePacketTester<ArtemisAnomaly> {
	@Test
	public void test() {
		execute(AnomalyParser.class, 2);
	}

	@Override
	protected void testObjects(List<ArtemisAnomaly> objects) {
		ArtemisAnomaly obj = objects.get(0);
		Assert.assertEquals(ObjectType.ANOMALY, obj.getType());
		Assert.assertNull(obj.getName());
		ArtemisAnomalyTest.assertAnomaly(obj, 1.0f, 2.0f, 3.0f, AnomalyType.CETROCITE_HEATSINKS);
		obj = objects.get(1);
		Assert.assertEquals(ObjectType.ANOMALY, obj.getType());
		Assert.assertNull(obj.getName());
		ArtemisAnomalyTest.assertAnomaly(obj, 2.0f, 3.0f, 1.0f, null);
	}

}
