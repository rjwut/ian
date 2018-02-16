package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Handles the three shield packets: shields up, shields down, and toggle shields.
 * @author rjwut
 */
public class SetShieldsPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		PacketFactory factory = new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SetShieldsPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SetShieldsPacket(reader);
			}
		};
		register(registry, SubType.TOGGLE_SHIELDS, factory);
		register(registry, SubType.SHIELDS_UP, factory);
		register(registry, SubType.SHIELDS_DOWN, factory);
	}

	public enum Action {
		TOGGLE(SubType.TOGGLE_SHIELDS),
		UP(SubType.SHIELDS_UP),
		DOWN(SubType.SHIELDS_DOWN);

		public static Action fromSubType(SubType subType) {
			switch(subType) {
			case TOGGLE_SHIELDS:
				return TOGGLE;
			case SHIELDS_UP:
				return UP;
			case SHIELDS_DOWN:
				return DOWN;
			default:
				throw new IllegalArgumentException("SubType " + subType + " not applicable");
			}
		}

		private SubType subType;

		private Action(SubType subType) {
			this.subType = subType;
		}
	}

	public SetShieldsPacket(Action action) {
        super(action != null ? action.subType : null, 0);

		if (action == null) {
			throw new IllegalArgumentException("You must specify an action");
		}
	}

    private SetShieldsPacket(PacketReader reader) {
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
