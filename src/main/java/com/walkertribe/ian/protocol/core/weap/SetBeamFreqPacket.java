package com.walkertribe.ian.protocol.core.weap;

import com.walkertribe.ian.enums.BeamFrequency;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Sets the frequency at which to tune the beams.
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.SET_BEAM_FREQUENCY)
public class SetBeamFreqPacket extends ValueIntPacket {
	/**
	 * @param frequency The desired beam frequency
	 */
    public SetBeamFreqPacket(BeamFrequency frequency) {
        super(frequency != null ? frequency.ordinal(): -1);

        if (frequency == null) {
        	throw new IllegalArgumentException(
        			"You must specify a beam frequency"
        	);
        }
    }

    public SetBeamFreqPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * The desired beam frequency
     */
    public BeamFrequency getBeamFrequency() {
    	return BeamFrequency.values()[mArg];
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(BeamFrequency.values()[mArg]);
	}
}