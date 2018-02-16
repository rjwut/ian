package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;

public class GameMasterTargetLocationPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.VALUE_FLOAT;
    private static final byte SUBTYPE = 0x06;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameMasterTargetLocationPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameMasterTargetLocationPacket(reader);
			}
		});
	}

    private float mX;
    private float mZ;
    private byte[] mUnknown;

    public GameMasterTargetLocationPacket(float x, float z) {
        super(ConnectionType.CLIENT, TYPE);
        mX = x;
        mZ = z;
        mUnknown = new byte[4];
    }

    private GameMasterTargetLocationPacket(PacketReader reader) {
    	super(ConnectionType.CLIENT, TYPE);
    	reader.skip(4); // subtype
    	mZ = reader.readFloat();
    	mUnknown = reader.readBytes(4);
    	mX = reader.readFloat();
    }

    /**
     * Return the X-coordinate for the selected location.
     */
    public float getX() {
    	return mX;
    }

    /**
     * Return the Z-coordinate for the selected location.
     */
    public float getZ() {
    	return mZ;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	writer
    		.writeInt(SUBTYPE)
    		.writeFloat(mZ)
    		.writeBytes(mUnknown)
    		.writeFloat(mX);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("x=").append(mX).append(" z=").append(mZ);
	}
}
