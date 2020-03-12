package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;

/**
 * A superclass for handling VALUE_INT client packets. Note that some packets
 * in the Artemis protocol technically have the valueInt type, but don't
 * actually follow the pattern of having a single int value. It may be that the
 * packets in question evolved over time and needed more values. Those packets
 * do not extend ValueIntPacket but are still mentioned in the SubType class.
 * @author rjwut
 */
public abstract class ValueIntPacket extends BaseArtemisPacket {
    /**
     * VALUE_INT client packet subtypes.
     */
    public static class SubType {
    	public static final byte WARP                    = 0x00;
    	public static final byte MAIN_SCREEN             = 0x01;
    	public static final byte WEAPONS_SELECT          = 0x02;
    	public static final byte TOGGLE_AUTO_BEAMS       = 0x03;
    	public static final byte TOGGLE_SHIELDS          = 0x04;
    	public static final byte SHIELDS_UP              = 0x05;
    	public static final byte SHIELDS_DOWN            = 0x06;
    	public static final byte REQUEST_DOCK            = 0x07;
    	public static final byte FIRE_TUBE               = 0x08;
    	public static final byte UNLOAD_TUBE             = 0x09;
    	public static final byte TOGGLE_RED_ALERT        = 0x0a;
    	public static final byte SET_BEAM_FREQUENCY      = 0x0b;
    	public static final byte SET_AUTO_DAMCON         = 0x0c;
    	public static final byte SET_SHIP                = 0x0d;
    	public static final byte SET_CONSOLE             = 0x0e;
    	public static final byte READY                   = 0x0f;
    	public static final byte SCIENCE_SELECT          = 0x10;
    	public static final byte CAPTAIN_SELECT          = 0x11;
    	public static final byte GM_SELECT               = 0x12;
    	public static final byte SCIENCE_SCAN            = 0x13;
    	public static final byte KEYSTROKE               = 0x14;
    	public static final byte BUTTON_CLICK            = 0x15;
    	public static final byte UNKNOWN_16              = 0x16;
    	public static final byte SHIP_SETUP              = 0x17;
    	public static final byte ENG_RESET_COOLANT       = 0x18;
    	public static final byte TOGGLE_REVERSE          = 0x19;
    	public static final byte REQUEST_ENG_GRID_UPDATE = 0x1a;
    	public static final byte TOGGLE_PERSPECTIVE      = 0x1b;
    	public static final byte ACTIVATE_UPGRADE        = 0x1c;
    	public static final byte CLIMB_DIVE              = 0x1d;
    	public static final byte SINGLE_SEAT_LAUNCH      = 0x1e;
    	public static final byte SINGLE_SEAT_SHOOT       = 0x1f;
    	public static final byte EMERGENCY_JUMP          = 0x20;
    	public static final byte SINGLE_SEAT_SETTINGS    = 0x21;
    	public static final byte BEACON_CONFIG           = 0x22;
    	public static final byte UNKNOWN_23              = 0x23;
    	public static final byte CLIENT_HEARTBEAT        = 0x24;

    	private SubType() {
    		// prevent instantiation
    	}
    }

    protected byte mSubType;
    protected int mArg = -1;

    /**
     * Use this constructor if the class services only one subtype.
     */
    public ValueIntPacket(int arg) {
        mSubType = getClass().getAnnotation(Packet.class).subtype()[0];
        mArg = arg;
    }

    /**
     * Use this constructor if the class services multiple subtypes.
     */
    public ValueIntPacket(byte subType, int arg) {
        mSubType = subType;
        mArg = arg;
    }

    protected ValueIntPacket(PacketReader reader) {
    	mSubType = (byte) reader.readInt();
    	mArg = reader.readInt();
    }

    @Override
	protected final void writePayload(PacketWriter writer) {
		writer.writeInt(mSubType);
		writer.writeInt(mArg);
	}
}