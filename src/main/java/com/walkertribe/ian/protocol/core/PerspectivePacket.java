package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Perspective;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Notifies clients that the main screen perspective has changed.
 * @author rjwut
 */
public class PerspectivePacket extends SimpleEventPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.PERSPECTIVE, new PacketFactory() {
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
    	super(SubType.PERSPECTIVE, reader);
        mPerspective = Perspective.values()[reader.readInt()];
    }

    public PerspectivePacket(Perspective perspective) {
    	super(SubType.PERSPECTIVE);

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
		super.writePayload(writer);
		writer.writeInt(mPerspective.ordinal());
	}
}