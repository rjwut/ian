package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

public class DmxMessagePacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.DMX_MESSAGE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return DmxMessagePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new DmxMessagePacket(reader);
			}
		});
	}

    private final CharSequence mName;
    private final boolean mOn;

    private DmxMessagePacket(PacketReader reader) {
        super(SubType.DMX_MESSAGE, reader);
        mName = reader.readString();
        mOn = reader.readInt() == 1;
    }

    public DmxMessagePacket(String name, boolean on) {
        super(SubType.DMX_MESSAGE);

        if (name == null || name.length() == 0) {
        	throw new IllegalArgumentException("You must provide a name");
        }

        mName = name;
        mOn = on;
    }

    /**
     * The name of the DMX flag.
     */
    public CharSequence getName() {
        return mName;
    }

    /**
     * Returns true if the DMX flag is on; false otherwise.
     */
    public boolean isOn() {
    	return mOn;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	super.writePayload(writer);
		writer.writeString(mName).writeInt(mOn ? 1 : 0);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mName).append('=').append(mOn ? "ON" : "OFF");
	}
}