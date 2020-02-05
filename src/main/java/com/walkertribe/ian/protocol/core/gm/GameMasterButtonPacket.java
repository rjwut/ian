package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ButtonClickPacket;
import com.walkertribe.ian.util.Util;

/**
 * Creates or removes a button on the game master console.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.GM_BUTTON, subtype = { 0x00, 0x01, 0x02, 0x64 })
public class GameMasterButtonPacket extends BaseArtemisPacket {
	/**
	 * The action being performed to this GM button.
	 */
	public enum Action {
		REMOVE,
		CREATE
	}

	private static final int SUBTYPE_POSITIONED = 0x02;
	private static final int SUBTYPE_REMOVE_ALL = 0x64;

	private Action mAction;
	private CharSequence mLabel;
	private int mX = -1, mY = -1, mW = -1, mH = -1;
	private ButtonClickPacket mClickPacket;

	/**
	 * Creates or removes a game master button; use Action.CREATE or
	 * Action.REMOVE to indicate which action you are performing. If creating
	 * a packet to remove all buttons on the screen, specify Action.REMOVE with
	 * a null label.
	 */
	public GameMasterButtonPacket(Action action, CharSequence label) {
        if (action == null) {
        	throw new IllegalArgumentException("Action is required");
        }

        if (label == null && action == Action.CREATE) {
        	throw new IllegalArgumentException("Label is required");
        }

        if (action != Action.REMOVE && Util.isBlank(label)) {
        	throw new IllegalArgumentException("Label is required");
        }

        mAction = action;
        mLabel = label;

        if (action == Action.CREATE) {
        	mClickPacket = new ButtonClickPacket(mLabel);
        }
	}

	/**
	 * Sets the position and dimensions for this button. If this method is not
	 * called, the client may position the button where it pleases.
	 */
	public void setRect(int x, int y, int w, int h) {
		if (mAction == Action.REMOVE) {
			throw new IllegalStateException("Button removal can't be positioned");
		}

		if (x < 0 || y < 0 || w < 1 || h < 1) {
			throw new IllegalArgumentException("Invalid rectangle");
		}

		mX = x;
		mY = y;
		mW = w;
		mH = h;
	}

	public GameMasterButtonPacket(PacketReader reader) {
        byte subtype = reader.readByte();
        boolean positioned = subtype == SUBTYPE_POSITIONED;
        boolean removeAll = subtype == SUBTYPE_REMOVE_ALL;

        if (positioned) {
        	mAction = Action.CREATE;
        } else if (removeAll) {
        	mAction = Action.REMOVE;
        } else {
            mAction = Action.values()[subtype];
        }

        if (!removeAll) {
        	mLabel = reader.readString();

        	if (mAction == Action.CREATE) {
            	mClickPacket = new ButtonClickPacket(mLabel);
        	}
        }

        if (positioned) {
        	mX = reader.readInt();
        	mY = reader.readInt();
        	mW = reader.readInt();
        	mH = reader.readInt();
        }
	}

	/**
	 * Returns the Action this packet is performing.
	 */
	public Action getAction() {
		return mAction;
	}

	/**
	 * Returns true if this packet declares that a button should be created in
	 * an assigned position and dimensions.
	 */
	public boolean isPositioned() {
		return mAction == Action.CREATE && mX != -1;
	}

	/**
	 * Returns true if this packet declares that all buttons should be removed.
	 */
	public boolean isRemoveAll() {
		return mAction == Action.REMOVE && mLabel == null;
	}

	/**
	 * The label for this button. This method will return null if isRemoveAll()
	 * returns true.
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

	/**
	 * The X-coordinate where the upper-left corner of the button should be
	 * positioned, or -1 if the packet doesn't create a positioned button.
	 */
	public int getX() {
		return mX;
	}

	/**
	 * The Y-coordinate where the upper-left corner of the button should be
	 * positioned, or -1 if the packet doesn't create a positioned button.
	 */
	public int getY() {
		return mY;
	}

	/**
	 * The width of the button in pixels, or -1 if the packet doesn't create a
	 * positioned button.
	 */
	public int getWidth() {
		return mW;
	}

	/**
	 * The height of the button in pixels, or -1 if the packet doesn't create a
	 * positioned button.
	 */
	public int getHeight() {
		return mH;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		byte subtype;

		if (isPositioned()) {
			subtype = SUBTYPE_POSITIONED;
		} else if (isRemoveAll()) {
			subtype = SUBTYPE_REMOVE_ALL;
		} else {
			subtype = (byte) mAction.ordinal();
		}

		writer.writeByte(subtype);

		if (!isRemoveAll()) {
			writer.writeString(mLabel);
		}

		if (isPositioned()) {
			writer
				.writeInt(mX)
				.writeInt(mY)
				.writeInt(mW)
				.writeInt(mH);
		}
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mAction);

		if (isRemoveAll()) {
			b.append(" ALL");
		} else {
			b.append(' ').append(mLabel);
		}

		if (isPositioned()) {
			b.append(" (")
			.append(mX)
			.append(',')
			.append(mY)
			.append(' ')
			.append(mW)
			.append('x')
			.append(mH)
			.append(')');
		}
	}
}
