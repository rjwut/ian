package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.world.Artemis;

/**
 * Unloads the indicated tube.
 */
public class UnloadTubePacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.UNLOAD_TUBE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return UnloadTubePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new UnloadTubePacket(reader);
			}
		});
	}

	/**
	 * @param tube Index of the tube to unload, [0 - Artemis.MAX_TUBES)
	 */
    public UnloadTubePacket(int tube) {
        super(SubType.UNLOAD_TUBE, tube);

        if (tube < 0 || tube >= Artemis.MAX_TUBES) {
        	throw new IndexOutOfBoundsException(
        			"Invalid tube index: " + tube
        	);
        }
    }

    private UnloadTubePacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the index of the tube to be unloaded.
     */
    public int getTubeIndex() {
    	return mArg;
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mArg);
	}
}