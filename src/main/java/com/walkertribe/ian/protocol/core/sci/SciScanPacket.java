package com.walkertribe.ian.protocol.core.sci;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Scans the indicated target.
 */
public class SciScanPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SCIENCE_SCAN, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SciScanPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SciScanPacket(reader);
			}
		});
	}

	/**
	 * @param target The target to scan
	 */
    public SciScanPacket(ArtemisObject target) {
        super(SubType.SCIENCE_SCAN, target != null ? target.getId() : 0);

        if (target == null) {
        	throw new IllegalArgumentException("You must provide a target");
        }
    }

    private SciScanPacket(PacketReader reader) {
        super(reader);
    }

    /**
     * Returns the ID of the target to be scanned.
     */
    public int getTargetId() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append('#').append(mArg);
	}
}