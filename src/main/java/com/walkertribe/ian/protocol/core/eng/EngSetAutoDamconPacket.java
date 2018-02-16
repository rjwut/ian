package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;

/**
 * Set whether DAMCON teams should be autonomous or not.
 * @author dhleong
 */
public class EngSetAutoDamconPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SET_AUTO_DAMCON, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return EngSetAutoDamconPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new EngSetAutoDamconPacket(reader);
			}
		});
	}

	/**
	 * @param autonomous Whether DAMCON teams should be autonomous
	 */
    public EngSetAutoDamconPacket(boolean autonomous) {
        super(SubType.SET_AUTO_DAMCON, autonomous ? 1 : 0);
    }

    private EngSetAutoDamconPacket(PacketReader reader) {
        super(reader);
    }

    public boolean isAutonomous() {
    	return mArg == 1;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg == 1 ? "on" : "off");
	}
}