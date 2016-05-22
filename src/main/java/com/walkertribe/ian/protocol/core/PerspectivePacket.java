package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.Perspective;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.UnexpectedTypeException;

/**
 * Notifies clients that the main screen perspective has changed.
 * @author rjwut
 */
public class PerspectivePacket extends BaseArtemisPacket {
	private static final int TYPE = 0xf754c8fe;
    private static final byte MSG_TYPE = 0x12;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, MSG_TYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return PerspectivePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new PerspectivePacket(reader);
			}
		});
	}

	private Perspective mPerspective;

	private PerspectivePacket(PacketReader reader) {
    	super(ConnectionType.SERVER, TYPE);
        int subtype = reader.readInt();

        if (subtype != MSG_TYPE) {
			throw new UnexpectedTypeException(subtype, MSG_TYPE);
        }

        mPerspective = Perspective.values()[reader.readInt()];
    }

    public PerspectivePacket(Perspective perspective) {
    	super(ConnectionType.SERVER, TYPE);

    	if (perspective == null) {
    		throw new IllegalArgumentException("You must provide a Perspective");
    	}

    	mPerspective = perspective;
    }

    /**
     * Returns the Perspective specified by this packet.
     */
    public Perspective getPerspective() {
    	return mPerspective;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mPerspective.name());
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(MSG_TYPE).writeInt(mPerspective.ordinal());
	}
}