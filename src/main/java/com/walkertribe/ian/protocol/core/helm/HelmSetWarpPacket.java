package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.world.Artemis;

/**
 * Set warp speed.
 * @author dhleong
 */
public class HelmSetWarpPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SubType.WARP,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return HelmSetWarpPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new HelmSetWarpPacket(reader);
			}
		});
	}

	/**
	 * @param warp Value between 0 (no warp) and 4 (max warp)
	 */
    public HelmSetWarpPacket(int warp) {
        super(SubType.WARP, warp);

        if (warp < 0 || warp > Artemis.MAX_WARP) {
        	throw new IndexOutOfBoundsException("Warp speed out of range");
        }
    }

    private HelmSetWarpPacket(PacketReader reader) {
        super(SubType.WARP, reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}