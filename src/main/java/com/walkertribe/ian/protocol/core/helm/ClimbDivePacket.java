package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;

/**
 * Changes the ship's trim, causing it to climb, dive or level out.
 * @author rjwut
 */
public class ClimbDivePacket extends ValueIntPacket {
    private static final int UP = -1;
    private static final int DOWN = 1;

	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.CLIMB_DIVE, new PacketFactory() {
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

    /**
     * Giving an "up" command while diving causes the ship to level out; giving
     * a second "up" command causes it to start climbing. The "down" command
     * does the reverse.
     * @param up True if you want to tilt the ship up, false to tilt it down.
     */
    public ClimbDivePacket(boolean up) {
        super(SubType.CLIMB_DIVE, up ? UP : DOWN);
    }

    private ClimbDivePacket(PacketReader reader) {
        super(reader);
    }

    public boolean isUp() {
    	return mArg == UP;
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg == UP ? "up" : "down");
	}
}
