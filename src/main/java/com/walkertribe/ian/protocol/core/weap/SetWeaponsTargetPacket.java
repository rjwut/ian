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
public class SetWeaponsTargetPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SET_TARGET, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SetWeaponsTargetPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SetWeaponsTargetPacket(reader);
			}
		});
	}

	/**
	 * @param target The desired target (or null to release target lock)
	 */
    public SetWeaponsTargetPacket(ArtemisObject target) {
        super(SubType.SET_TARGET, target == null ? 1 : target.getId());
    }

    private SetWeaponsTargetPacket(PacketReader reader) {
    	super(SubType.SET_TARGET, reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mArg);
	}
}