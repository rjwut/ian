package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisBlackHole;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * ObjectParser implementation for black holes
 * @author rjwut
 */
public class BlackHoleParser extends AbstractObjectParser {
    private enum Bit {
        X,
        Y,
        Z
    }
    private static final int BIT_COUNT = Bit.values().length;

    BlackHoleParser() {
        super(ObjectType.BLACK_HOLE);
    }

    @Override
    public int getBitCount() {
        return BIT_COUNT;
    }

    @Override
    protected ArtemisBlackHole parseImpl(PacketReader reader) {
        final ArtemisBlackHole obj = new ArtemisBlackHole(reader.getObjectId());
        obj.setX(reader.readFloat(Bit.X));
        obj.setY(reader.readFloat(Bit.Y));
        obj.setZ(reader.readFloat(Bit.Z));
        return obj;
    }

    @Override
    public void write(ArtemisObject obj, PacketWriter writer) {
        ArtemisBlackHole gObj = (ArtemisBlackHole) obj;
        writer  .writeFloat(Bit.X, gObj.getX())
                .writeFloat(Bit.Y, gObj.getY())
                .writeFloat(Bit.Z, gObj.getZ());
    }
}
