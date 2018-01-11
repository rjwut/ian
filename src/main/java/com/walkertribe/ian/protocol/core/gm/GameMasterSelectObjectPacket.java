package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Selects (or deselects) a target on the game master's map.
 * @author rjwut
 */
public class GameMasterSelectObjectPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.GM_SELECT, new PacketFactory() {
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
        super(SubType.GM_SELECT, target == null ? 1 : target.getId());
    }

    private GameMasterSelectObjectPacket(PacketReader reader) {
    	super(SubType.GM_SELECT, reader);
    }

    /**
     * Returns the ID of the selected object, or 1 if the previously-selected
     * object was deselected.
     */
    public int getTargetId() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
    	if (mArg == 1) {
    		b.append("DESELECTED");
    	} else {
    		b.append('#').append(mArg);
    	}
	}
}