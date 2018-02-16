package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;
import com.walkertribe.ian.world.ArtemisObject;

public class ExplosionPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.EXPLOSION, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SoundEffectPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new ExplosionPacket(reader);
			}
		});
	}

	private ObjectType mObjectType;
	private int mObjectId;

	private ExplosionPacket(PacketReader reader) {
		super(SubType.EXPLOSION, reader);
		
	}

	public ExplosionPacket(ArtemisObject obj) {
		this(obj.getType(), obj.getId());
	}

	public ExplosionPacket(ObjectType objectType, int objectId) {
		super(SubType.EXPLOSION);

		if (objectType == null) {
			throw new IllegalArgumentException("You must provide an object type");
		}

		if (objectId < 0) {
			throw new IllegalArgumentException("Invalid object ID: " + objectId);
		}

		mObjectType = objectType;
		mObjectId = objectId;
	}

	public ObjectType getObjectType() {
		return mObjectType;
	}

	public int getObjectId() {
		return mObjectId;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeByte((byte) mObjectType.getId()).writeInt(mObjectId);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mObjectType).append(" #").append(mObjectId);
	}

}
