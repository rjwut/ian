package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.world.ArtemisAsteroid;
import com.walkertribe.ian.world.ArtemisObjectTest;

public class AsteroidParserTest extends AbstractObjectUpdatePacketTester<ArtemisAsteroid> {
    @Test
    public void test() {
        execute(AsteroidParser.class, 1);
    }

    @Override
    protected void testObjects(List<ArtemisAsteroid> objects) {
        ArtemisObjectTest.assertObject(objects.get(0), 2, ObjectType.ASTEROID, null, 1.0f, 2.0f, 3.0f);
    }
}
