package com.walkertribe.ian.protocol.core.weap;

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
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Manually fire beams.
 */
public class FireBeamPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.BEAM_REQUEST;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return FireBeamPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new FireBeamPacket(reader);
			}
		});
	}

	private int mId;
	private float mX;
	private float mY;
	private float mZ;

    /**
     * Fire at the given target. The x, y and z parameters indicate the
     * coordinates on the target at which to fire.
     */
    public FireBeamPacket(ArtemisObject target, float x, float y, float z) {
        super(ConnectionType.CLIENT, TYPE);
        mId = target.getId();
        mX = x;
        mY = y;
        mZ = z;
    }

    private FireBeamPacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
        mId = reader.readInt();
        mX = reader.readFloat();
        mY = reader.readFloat();
        mZ = reader.readFloat();
    }

    /**
     * Returns the ID of the beam target.
     */
    public int getTargetId() {
    	return mId;
    }

    /**
     * Returns the X-coordinate of the beam endpoint relative to the target's
     * model origin.
     */
    public float getX() {
    	return mX;
    }

    /**
     * Returns the Y-coordinate of the beam endpoint relative to the target's
     * model origin.
     */
    public float getY() {
    	return mY;
    }

    /**
     * Returns the Z-coordinate of the beam endpoint relative to the target's
     * model origin.
     */
    public float getZ() {
    	return mZ;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mId).writeFloat(mX).writeFloat(mY).writeFloat(mZ);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#')
		.append(mId)
		.append(": ")
		.append(mX)
		.append(',')
		.append(mY)
		.append(',')
		.append(mZ);
	}
}