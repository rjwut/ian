package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.SKYBOX)
public class SkyboxPacket extends SimpleEventPacket {
	private int mSkyboxId;

    public SkyboxPacket(int skyboxId) {
        super(SubType.SKYBOX);
        mSkyboxId = skyboxId;
    }

	public SkyboxPacket(PacketReader reader) {
        super(reader);
        mSkyboxId = reader.readInt();
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