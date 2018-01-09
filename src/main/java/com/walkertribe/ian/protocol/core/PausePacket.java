package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.util.BoolState;

public class PausePacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.PAUSE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return PausePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new PausePacket(reader);
			}
		});
	}

    private final BoolState mPaused;
    
    private PausePacket(PacketReader reader) {
        super(SubType.PAUSE, reader);
        mPaused = reader.readBool(4);
    }

    public PausePacket(boolean paused) {
        super(SubType.PAUSE);
    	mPaused = BoolState.from(paused);
    }

    public BoolState getPaused() {
        return mPaused;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeInt(BoolState.safeValue(mPaused) ? 1 : 0);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(BoolState.safeValue(mPaused) ? "PAUSED" : "RUNNING");
	}
}