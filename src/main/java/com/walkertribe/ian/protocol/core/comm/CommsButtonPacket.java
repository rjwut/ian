package com.walkertribe.ian.protocol.core.comm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.ButtonClickPacket;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.TextUtil;
import com.walkertribe.ian.util.Util;

@Packet(origin = Origin.SERVER, type = CorePacketType.COMMS_BUTTON)
public class CommsButtonPacket extends BaseArtemisPacket {
	public enum Action {
		REMOVE,
		CREATE
	}

	private Action mAction;
	private CharSequence mLabel;
	private ButtonClickPacket mClickPacket;

	/**
	 * Create or remove buttons on the comm screen. If you are removing all buttons, pass in null for the label.
	 */
	public CommsButtonPacket(Action action, CharSequence label) {
		if (action == null) {
			throw new IllegalArgumentException("You must specify an Action");
		}

		if (action == Action.CREATE && Util.isBlank(label)) {
			throw new IllegalArgumentException("You must specify an label");
		}

		mAction = action;
		mLabel = label;

		if (action == Action.CREATE) {
			mClickPacket = new ButtonClickPacket(mLabel);
		}
	}

	public CommsButtonPacket(PacketReader reader) {
		byte actionValue = reader.readByte();

		switch (actionValue) {
		case 0x00:
		case 0x64:
			mAction = Action.REMOVE;
			break;
		case 0x02:
			mAction = Action.CREATE;
			break;
		default:
			throw new IllegalArgumentException("Unknown action: " + TextUtil.byteToHex(actionValue));
		}

		if (actionValue != 0x64) {
			mLabel = reader.readString();
		}
	}

	/**
	 * Returns whether to add or remove button(s).
	 */
	public Action getAction() {
		return mAction;
	}

	/**
	 * Returns true if this packet declares that all buttons should be removed.
	 */
	public boolean isRemoveAll() {
		return mAction == Action.REMOVE && mLabel == null;
	}

	/**
	 * The label of the button to add/remove. If this packet is removing all buttons, getLabel() returns null.
	 */
	public CharSequence getLabel() {
		return mLabel;
	}

	/**
	 * Returns the ButtonClickPacket that should be sent when the user clicks
	 * the button described by this packet, or null if this packet does not
	 * describe a button's creation.
	 */
	public ButtonClickPacket buildClickPacket() {
		return mClickPacket;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeByte((byte) (Action.REMOVE.equals(mAction) ? (mLabel == null ? 0x64 : 0x00) : 0x02));

		if (mLabel != null) {
			writer.writeString(mLabel);
		}
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		if (mLabel == null) {
			b.append("REMOVE ALL");
		} else {
			b.append(mAction.name()).append(' ').append(mLabel);
		}
	}
}
