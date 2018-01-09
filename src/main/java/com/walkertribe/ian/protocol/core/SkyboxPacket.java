package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

public class SkyboxPacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SKYBOX, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SkyboxPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SkyboxPacket(reader);
			}
		});
	}

	private int mSkyboxId;

	private SkyboxPacket(PacketReader reader) {
        super(SubType.SKYBOX, reader);
        mSkyboxId = reader.readInt();
	}

    public SkyboxPacket(int skyboxId) {
        super(SubType.SKYBOX);
        mSkyboxId = skyboxId;
    }

    public int getSkyboxId() {
    	return mSkyboxId;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	super.writePayload(writer);
		writer.writeInt(mSkyboxId);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mSkyboxId);
	}
}