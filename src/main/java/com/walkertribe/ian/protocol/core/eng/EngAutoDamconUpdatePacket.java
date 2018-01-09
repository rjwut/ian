package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.SimpleEventPacket;

public class EngAutoDamconUpdatePacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.AUTO_DAMCON, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return EngAutoDamconUpdatePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new EngAutoDamconUpdatePacket(reader);
			}
		});
	}

    private final boolean mOn;

    private EngAutoDamconUpdatePacket(PacketReader reader) {
        super(SubType.AUTO_DAMCON, reader);
        mOn = reader.readInt() == 1;
    }

    public EngAutoDamconUpdatePacket(boolean on) {
        super(SubType.AUTO_DAMCON);
        mOn = on;
    }

    /**
     * Returns true if autonomous DAMCON was turned on; false if turned off.
     */
    public boolean isOn() {
    	return mOn;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	super.writePayload(writer);
		writer.writeInt(mOn ? 1 : 0);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mOn ? "ON" : "OFF");
	}
}