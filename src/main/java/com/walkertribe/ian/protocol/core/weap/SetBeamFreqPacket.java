package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.core.ValueIntPacket;

/**
 * Sets the frequency at which to tune the beams.
 */
public class SetBeamFreqPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.SET_BEAM_FREQUENCY, new PacketFactory() {
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
        super(SubType.SET_BEAM_FREQUENCY, frequency != null ? frequency.ordinal(): -1);

        if (frequency == null) {
        	throw new IllegalArgumentException(
        			"You must specify a beam frequency"
        	);
        }
    }

    private SetBeamFreqPacket(PacketReader reader) {
    	super(reader);
    }

    public BeamFrequency getBeamFrequency() {
    	return BeamFrequency.values()[mArg];
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(BeamFrequency.values()[mArg]);
	}
}