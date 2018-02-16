package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Set the target for our weapons.
 * @author dhleong
 */
public class WeaponsTargetPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.WEAPONS_SELECT, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return WeaponsTargetPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new WeaponsTargetPacket(reader);
			}
		});
	}

	/**
	 * @param target The desired target (or null to release target lock)
	 */
    public WeaponsTargetPacket(ArtemisObject target) {
        super(SubType.WEAPONS_SELECT, target == null ? 1 : target.getId());
    }

    private WeaponsTargetPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the target's ID.
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