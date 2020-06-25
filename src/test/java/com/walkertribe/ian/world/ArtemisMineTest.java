package com.walkertribe.ian.world;

import org.junit.Test;

import com.walkertribe.ian.enums.ObjectType;

public class ArtemisMineTest {
    @Test
    public void testUpdateFrom() {
        ArtemisMine obj0 = new ArtemisMine(47);
        ArtemisObjectTest.assertUnknownObject(obj0, 47, ObjectType.MINE);
        ArtemisMine obj1 = new ArtemisMine(47);
        obj1.setX(1.0f);
        obj1.setY(2.0f);
        obj1.setZ(3.0f);
        ArtemisObjectTest.assertObject(obj1, 47, ObjectType.MINE, null, 1.0f, 2.0f, 3.0f);
        obj1.updateFrom(obj0);
        ArtemisObjectTest.assertObject(obj1, 47, ObjectType.MINE, null, 1.0f, 2.0f, 3.0f);
        obj0.updateFrom(obj1);
        ArtemisObjectTest.assertObject(obj0, 47, ObjectType.MINE, null, 1.0f, 2.0f, 3.0f);
        obj0.updateFrom(new ArtemisAnomaly(48));
    }
}
