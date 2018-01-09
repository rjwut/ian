package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;

/**
 * A superclass for handling all VALUE_INT client packets.
 * @author rjwut
 */
public abstract class ValueIntPacket extends BaseArtemisPacket {
    protected static final PacketType TYPE = CorePacketType.VALUE_INT;

    /**
     * VALUE_INT client packet subtypes.
     * The order of these enum values matters! Do not reorder!
     */
    public enum SubType {
    	WARP,                    // 00
    	MAIN_SCREEN,             // 01
    	SET_TARGET,              // 02
    	TOGGLE_AUTO_BEAMS,       // 03
    	TOGGLE_SHIELDS,          // 04
    	UNKNOWN_05,              // 05
    	UNKNOWN_06,              // 06
    	REQUEST_DOCK,            // 07
    	FIRE_TUBE,               // 08
    	UNLOAD_TUBE,             // 09
    	TOGGLE_RED_ALERT,        // 0a
    	SET_BEAM_FREQUENCY,      // 0b
    	SET_AUTO_DAMCON,         // 0c
    	SET_SHIP,                // 0d
    	SET_CONSOLE,             // 0e
    	READY,                   // 0f
    	SCIENCE_SELECT,          // 10
    	CAPTAIN_SELECT,          // 11
    	GM_SELECT,               // 12
    	SCIENCE_SCAN,            // 13
    	KEYSTROKE,               // 14
    	GM_BUTTON_CLICK,         // 15
    	SHIP_SETUP,              // 16
    	UNKNOWN_17,              // 17
    	TOGGLE_REVERSE,          // 18
    	REQUEST_ENG_GRID_UPDATE, // 19
    	TOGGLE_PERSPECTIVE,      // 1a
    	CLIMB_DIVE,              // 1b
    	UNKNOWN_1C,              // 1c
    	FIGHTER_LAUNCH           // 1d
    }

    private SubType mSubType;
    protected int mArg = -1;

    /**
     * Use this constructor if you intend to override writePayload() with your
     * own implementation and not call ValueIntPacket.writePayload(). This may
     * be useful in cases where a ValueIntPacket, for some reason, does not
     * contain just one int.
     */
    public ValueIntPacket(SubType subType) {
        super(ConnectionType.CLIENT, TYPE);
        mSubType = subType;
    }

    /**
     * Use this constructor if the packet has a single int argument that is
     * written to the payload after the SubType. In this case, you will not need
     * to override writePayload().
     */
    public ValueIntPacket(SubType subType, int arg) {
        this(subType);
        mArg = arg;
    }

    protected ValueIntPacket(SubType subType, PacketReader reader) {
    	this(subType);
    	reader.skip(4); // subtype
    	mArg = reader.readInt();
    }

    @Override
	protected void writePayload(PacketWriter writer) {
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