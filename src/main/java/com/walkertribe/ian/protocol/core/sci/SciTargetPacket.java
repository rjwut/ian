package com.walkertribe.ian.protocol.core.sci;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Sets the science officer's current target.
 */
public class SciTargetPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SCIENCE_SELECT, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SciTargetPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SciTargetPacket(reader);
			}
		});
	}

	/**
	 * @param target The target to select (or null to clear the target)
	 */
    public SciTargetPacket(ArtemisObject target) {
        super(SubType.SCIENCE_SELECT, target == null ? 1 : target.getId());
    }

    private SciTargetPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the ID of the selected target, or 1 if the previously-selected
     * target has been deselected.
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