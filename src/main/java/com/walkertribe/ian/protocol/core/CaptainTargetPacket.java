package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Selects (or deselects) a target on the captain's map.
 * @author rjwut
 */
public class CaptainTargetPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.CAPTAIN_SELECT, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return CaptainTargetPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new CaptainTargetPacket(reader);
			}
		});
	}

	/**
	 * @param target The target to select, or null to deselect a target
	 */
    public CaptainTargetPacket(ArtemisObject target) {
        super(SubType.CAPTAIN_SELECT, target == null ? 1 : target.getId());
    }

    private CaptainTargetPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the ID of the selected target, or 1 if the previous target was
     * deselected.
     */
    public int getTargetId() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
    	if (mArg == 1) {
    		b.append("no target");
    	} else {
    		b.append('#').append(mArg);
    	}
	}
}