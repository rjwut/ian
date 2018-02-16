package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;

/**
 * A superclass for handling VALUE_INT client packets. Note that some packets
 * in the Artemis protocol technically have the valueInt type, but don't
 * actually follow the pattern of having a single int value. It may be that the
 * packets in question evolved over time and needed more values. Those packets
 * do not extend ValueIntPacket but are still mentioned in the SubType enum.
 * @author rjwut
 */
public abstract class ValueIntPacket extends BaseArtemisPacket {
    protected static final PacketType TYPE = CorePacketType.VALUE_INT;

    /**
     * VALUE_INT client packet subtypes.
     * The order of these enum values matters! Do not reorder!
     * Values marked with an asterisk correspond to packet types that don't
     * extend ValueIntPacket.
     */
    public enum SubType {
    	WARP,                    // 00
    	MAIN_SCREEN,             // 01
    	WEAPONS_SELECT,          // 02
    	TOGGLE_AUTO_BEAMS,       // 03
    	TOGGLE_SHIELDS,          // 04
    	SHIELDS_UP,              // 05
    	SHIELDS_DOWN,            // 06
    	REQUEST_DOCK,            // 07
    	FIRE_TUBE,               // 08
    	UNLOAD_TUBE,             // 09
    	TOGGLE_RED_ALERT,        // 0a
    	SET_BEAM_FREQUENCY,      // 0b
    	SET_AUTO_DAMCON,         // 0c
    	SET_SHIP,                // 0d
    	SET_CONSOLE,             // 0e *
    	READY,                   // 0f
    	SCIENCE_SELECT,          // 10
    	CAPTAIN_SELECT,          // 11
    	GM_SELECT,               // 12
    	SCIENCE_SCAN,            // 13
    	KEYSTROKE,               // 14
    	GM_BUTTON_CLICK,         // 15 *
    	UNKNOWN_16,              // 16
    	SHIP_SETUP,              // 17 *
    	ENG_RESET,               // 18 TODO
    	TOGGLE_REVERSE,          // 19
    	REQUEST_ENG_GRID_UPDATE, // 1a
    	TOGGLE_PERSPECTIVE,      // 1b
    	ACTIVATE_UPGRADE,        // 1c
    	CLIMB_DIVE,              // 1d
    	SINGLE_SEAT_LAUNCH       // 1e
    }

    protected SubType mSubType;
    protected int mArg = -1;

    /**
     * Use this constructor if the packet has a single int argument that is
     * written to the payload after the SubType. In this case, you will not need
     * to override writePayload().
     */
    public ValueIntPacket(SubType subType, int arg) {
        this(subType);
        mArg = arg;
    }

    protected ValueIntPacket(PacketReader reader) {
    	this(SubType.values()[reader.readInt()]);
    	mArg = reader.readInt();
    }

    /**
     * Common private constructor
     */
    private ValueIntPacket(SubType subType) {
        super(ConnectionType.CLIENT, TYPE);
        mSubType = subType;
    }

    @Override
	protected final void writePayload(PacketWriter writer) {
		writer.writeInt(mSubType.ordinal());
		writer.writeInt(mArg);
	}

    /**
     * Convenience method for coercing the SubType enum to a byte.
     */
    protected static void register(PacketFactoryRegistry registry, SubType subType,
    		PacketFactory factory) {
    	registry.register(ConnectionType.CLIENT, TYPE, (byte) subType.ordinal(), factory);
    }
}