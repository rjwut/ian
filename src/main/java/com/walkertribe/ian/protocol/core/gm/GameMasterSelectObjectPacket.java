package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ShipActionPacket;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Selects (or deselects) a target on the game master's map.
 * @author rjwut
 */
public class GameMasterSelectObjectPacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_GM_SELECT,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameMasterSelectObjectPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameMasterSelectObjectPacket(reader);
			}
		});
	}

	/**
	 * @param target The target to select, or null to deselect a target
	 */
    public GameMasterSelectObjectPacket(ArtemisObject target) {
        super(TYPE_GM_SELECT, target == null ? 1 : target.getId());
    }

    private GameMasterSelectObjectPacket(PacketReader reader) {
    	super(TYPE_GM_SELECT, reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mArg);
	}
}