package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ShipActionPacket;

/**
 * Sets the frequency at which to tune the beams.
 */
public class SetBeamFreqPacket extends ShipActionPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, TYPE_SET_BEAMFREQ,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SetBeamFreqPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SetBeamFreqPacket(reader);
			}
		});
	}

	/**
	 * @param frequency The desired beam frequency
	 */
    public SetBeamFreqPacket(BeamFrequency frequency) {
        super(TYPE_SET_BEAMFREQ, frequency != null ? frequency.ordinal(): -1);

        if (frequency == null) {
        	throw new IllegalArgumentException(
        			"You must specify a beam frequency"
        	);
        }
    }

    private SetBeamFreqPacket(PacketReader reader) {
    	super(TYPE_SET_BEAMFREQ, reader);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(BeamFrequency.values()[mArg]);
	}
}