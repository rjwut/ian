package com.walkertribe.ian.protocol.core.world;

import java.util.List;

import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.world.ArtemisBlackHole;
import com.walkertribe.ian.world.ArtemisObjectTest;

public class BlackHoleParserTest extends AbstractObjectUpdatePacketTester<ArtemisBlackHole> {
    @Test
    public void test() {
        execute(BlackHoleParser.class, 1);
    }

    @Override
    protected void testObjects(List<ArtemisBlackHole> objects) {
        ArtemisObjectTest.assertObject(objects.get(0), 2, ObjectType.BLACK_HOLE, null, 1.0f, 2.0f, 3.0f);
    }
}
