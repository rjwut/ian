package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;

public class CloakDecloakPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.CLOAK_DECLOAK, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return CloakDecloakPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new CloakDecloakPacket(reader);
			}
		});
	}

    private final float mX;
    private final float mY;
    private final float mZ;

    private CloakDecloakPacket(PacketReader reader) {
        super(SubType.CLOAK_DECLOAK, reader);
        mX = reader.readFloat();
        mY = reader.readFloat();
        mZ = reader.readFloat();
    }

    public CloakDecloakPacket(float x, float y, float z) {
        super(SubType.CLOAK_DECLOAK);
        mX = x;
        mY = y;
        mZ = z;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public float getZ() {
        return mZ;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeFloat(mX).writeFloat(mY).writeFloat(mZ);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('(').append(mX).append(',').append(mY).append(',').append(mZ).append(')');
	}
}
