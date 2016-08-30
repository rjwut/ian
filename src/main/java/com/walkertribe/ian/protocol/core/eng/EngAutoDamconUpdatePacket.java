package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;

public class EngAutoDamconUpdatePacket extends BaseArtemisPacket {
    private static final int TYPE = 0xf754c8fe;
    private static final byte MSG_TYPE = 0x0b;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, MSG_TYPE,
				new PacketFactory() {
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
        super(ConnectionType.SERVER, TYPE);
        reader.skip(4); // subtype
        mOn = reader.readInt() == 1;
    }

    public EngAutoDamconUpdatePacket(boolean on) {
        super(ConnectionType.SERVER, TYPE);
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
		writer.writeInt(MSG_TYPE).writeInt(mOn ? 1 : 0);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mOn ? "ON" : "OFF");
	}
}