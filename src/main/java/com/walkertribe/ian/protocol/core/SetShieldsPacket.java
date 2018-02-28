package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Handles the three shield packets: shields up, shields down, and toggle shields.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = {
		SubType.TOGGLE_SHIELDS, SubType.SHIELDS_UP, SubType.SHIELDS_DOWN
})
public class SetShieldsPacket extends ValueIntPacket {
	/**
	 * What shield action to perform
	 */
	public enum Action {
		TOGGLE(SubType.TOGGLE_SHIELDS),
		UP(SubType.SHIELDS_UP),
		DOWN(SubType.SHIELDS_DOWN);

		public static Action fromSubType(byte subType) {
			switch(subType) {
			case SubType.TOGGLE_SHIELDS:
				return TOGGLE;
			case SubType.SHIELDS_UP:
				return UP;
			case SubType.SHIELDS_DOWN:
				return DOWN;
			default:
				throw new IllegalArgumentException("SubType " + subType + " not applicable");
			}
		}

		private byte subType;

		private Action(byte subType) {
			this.subType = subType;
		}
	}

	public SetShieldsPacket(Action action) {
        super(action != null ? action.subType : 0, 0);

		if (action == null) {
			throw new IllegalArgumentException("You must specify an action");
		}
	}

    public SetShieldsPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * The shield action to perform: up, down, or toggle.
     */
    public Action getAction() {
    	return Action.fromSubType(mSubType);
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(getAction());
	}
}
