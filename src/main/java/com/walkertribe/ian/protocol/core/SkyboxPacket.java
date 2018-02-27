package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;

/**
 * Indicates which skybox to use. The skybox files are located in the "art"
 * directory in the Artemis install, under subdirectories whose names are "sb"
 * followed by a two-digit decimal value, which is the skybox ID.
 * @author rjwut
 */
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

	/**
	 * The ID of the skybox to use.
	 */
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