package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisMine;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * ObjectParser implementation for mines
 * @author rjwut
 */
public class MineParser extends AbstractObjectParser {
    private enum Bit {
        X,
        Y,
        Z
    }
    private static final int BIT_COUNT = Bit.values().length;

    MineParser() {
        super(ObjectType.MINE);
    }

    @Override
    public int getBitCount() {
        return BIT_COUNT;
    }

    @Override
    protected ArtemisMine parseImpl(PacketReader reader) {
        final ArtemisMine obj = new ArtemisMine(reader.getObjectId());
        obj.setX(reader.readFloat(Bit.X));
        obj.setY(reader.readFloat(Bit.Y));
        obj.setZ(reader.readFloat(Bit.Z));
        return obj;
    }

    @Override
    public void write(ArtemisObject obj, PacketWriter writer) {
        ArtemisMine gObj = (ArtemisMine) obj;
        writer  .writeFloat(Bit.X, gObj.getX())
                .writeFloat(Bit.Y, gObj.getY())
                .writeFloat(Bit.Z, gObj.getZ());
    }
}
