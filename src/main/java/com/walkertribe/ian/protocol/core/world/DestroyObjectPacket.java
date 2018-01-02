package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.ObjectType;
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
 * Sent by the server when an object is destroyed.
 */
public class DestroyObjectPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.OBJECT_DELETE;
    
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return DestroyObjectPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new DestroyObjectPacket(reader);
			}
		});
	}

    private final ObjectType mTargetType;
    private final int mTarget;

    private DestroyObjectPacket(PacketReader reader) {
    	this(ObjectType.fromId(reader.readByte()), reader.readInt());
    }

    public DestroyObjectPacket(ArtemisObject obj) {
    	this(obj.getType(), obj.getId());
    }

    public DestroyObjectPacket(ObjectType targetType, int id) {
        super(ConnectionType.SERVER, TYPE);
        mTargetType = targetType;
        mTarget = id;
    }

    /**
     * The ObjectType of the destroyed object
     */
    public ObjectType getTargetType() {
        return mTargetType;
    }

    /**
     * The destroyed object's ID
     */
    public int getTarget() {
        return mTarget;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeByte(mTargetType.getId()).writeInt(mTarget);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mTarget).append(" (").append(mTargetType).append(')');
	}
}