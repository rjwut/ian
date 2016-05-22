package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.UnexpectedTypeException;

/**
 * Changes the ship's trim, causing it to climb, dive or level out.
 * @author rjwut
 */
public class ClimbDivePacket extends BaseArtemisPacket {
    private static final int TYPE = 0x4C821D3C;
    private static final byte SUBTYPE = 0x1b;
    private static final int UP = -1;
    private static final int DOWN = 1;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return ClimbDivePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new ClimbDivePacket(reader);
			}
		});
	}

    private boolean mUp;

    /**
     * Giving an "up" command while diving causes the ship to level out; giving
     * a second "up" command causes it to start climbing. The "down" command
     * does the reverse.
     * @param up True if you want to tilt the ship up, false to tilt it down.
     */
    public ClimbDivePacket(boolean up) {
        super(ConnectionType.CLIENT, TYPE);
        this.mUp = up;
    }

    private ClimbDivePacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
        int subtype = reader.readInt();

        if (subtype != SUBTYPE) {
        	throw new UnexpectedTypeException(subtype, SUBTYPE);
        }

        mUp = reader.readInt() == UP;
    }

    public boolean isUp() {
    	return mUp;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(SUBTYPE).writeInt(mUp ? UP : DOWN);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mUp ? "up" : "down");
	}
}
