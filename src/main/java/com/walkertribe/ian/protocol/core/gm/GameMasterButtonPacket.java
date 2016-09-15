package com.walkertribe.ian.protocol.core.gm;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;

/**
 * Creates or removes a button on the game master console.
 * @author rjwut
 */
public class GameMasterButtonPacket extends BaseArtemisPacket {
	public enum Action {
		REMOVE,
		CREATE
	}

	private static final int TYPE = 0x26faacb9;
	private static final int SUBTYPE_POSITIONED = 0x02;

	public static void register(PacketFactoryRegistry registry) {
		PacketFactory factory = new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameMasterButtonPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameMasterButtonPacket(reader);
			}
		};

		for (byte i = 0; i < 3; i++) {
			registry.register(ConnectionType.SERVER, TYPE, i, factory);
		}
	}

	private Action mAction;
	private String mLabel;
	private int mX = -1, mY = -1, mW = -1, mH = -1;
	private GameMasterButtonClickPacket mClickPacket;

	/**
	 * Creates or removes a game master button; use Action.CREATE or
	 * Action.REMOVE to indicate which action you are performing.
	 */
	public GameMasterButtonPacket(Action action, String label) {
        super(ConnectionType.SERVER, TYPE);

        if (action == null) {
        	throw new IllegalArgumentException("Action is required");
        }

        if (label == null || label.length() == 0) {
        	throw new IllegalArgumentException("Label is required");
        }

        mAction = action;
        mLabel = label;
		mClickPacket = new GameMasterButtonClickPacket(mLabel);
	}

	/**
	 * Sets the position and dimensions for this button. If this method is not
	 * called, the client may position the button where it pleases.
	 */
	public void setRect(int x, int y, int w, int h) {
		if (x < 1 || y < 1 || w < 1 || h < 1) {
			throw new IllegalArgumentException("Invalid rectangle");
		}

		mX = x;
		mY = y;
		mW = w;
		mH = h;
	}

	private GameMasterButtonPacket(PacketReader reader) {
        super(ConnectionType.SERVER, TYPE);
        byte subtype = reader.readByte();
        boolean positioned = subtype == SUBTYPE_POSITIONED;

        if (positioned) {
        	mAction = Action.CREATE;
        } else {
            mAction = Action.values()[subtype];
        }

        mLabel = reader.readString();
		mClickPacket = new GameMasterButtonClickPacket(mLabel);

        if (positioned) {
        	mW = reader.readInt();
        	mH = reader.readInt();
        	mX = reader.readInt();
        	mY = reader.readInt();
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
		return mX != -1;
	}

	/**
	 * The label for this button.
	 */
	public String getLabel() {
		return mLabel;
	}

	/**
	 * Returns the GameMasterButtonClickPacket that should be sent when the user
	 * clicks the button described by this packet.
	 */
	public GameMasterButtonClickPacket buildClickPacket() {
		return mClickPacket;
	}

	/**
	 * The X-coordinate where the upper-left corner of the button should be
	 * positioned, or -1 if the client can decide for itself or the button is
	 * being removed.
	 */
	public int getX() {
		return mX;
	}

	/**
	 * The Y-coordinate where the upper-left corner of the button should be
	 * positioned, or -1 if the client can decide for itself or the button is
	 * being removed.
	 */
	public int getY() {
		return mY;
	}

	/**
	 * The width of the button in pixels, or -1 if the client can decide for
	 * itself or the button is being removed.
	 */
	public int getWidth() {
		return mW;
	}

	/**
	 * The height of the button in pixels, or -1 if the client can decide for
	 * itself or the button is being removed.
	 */
	public int getHeight() {
		return mH;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer
			.writeInt(isPositioned() ? SUBTYPE_POSITIONED : mAction.ordinal())
			.writeString(mLabel);

		if (isPositioned()) {
			writer
				.writeInt(mW)
				.writeInt(mH)
				.writeInt(mX)
				.writeInt(mY);
		}
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mAction).append(' ').append(mLabel);

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
