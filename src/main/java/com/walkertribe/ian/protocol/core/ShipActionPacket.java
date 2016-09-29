package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;

/**
 * A superclass for handling all type 0x4c821d3c client packets.
 * @author rjwut
 */
public abstract class ShipActionPacket extends BaseArtemisPacket {
    protected static final int TYPE = 0x4c821d3c;

    protected static final byte TYPE_WARPSPEED               = 0x00;
    protected static final byte TYPE_MAINSCREEN              = 0x01;
    protected static final byte TYPE_SET_TARGET              = 0x02;
    protected static final byte TYPE_TOGGLE_AUTO_BEAMS       = 0x03;
    protected static final byte TYPE_TOGGLE_SHIELDS          = 0x04;

    protected static final byte TYPE_REQUEST_DOCK            = 0x07;
    protected static final byte TYPE_FIRE_TUBE               = 0x08;
    protected static final byte TYPE_UNLOAD_TUBE             = 0x09;
    protected static final byte TYPE_TOGGLE_REDALERT         = 0x0a;
    protected static final byte TYPE_SET_BEAMFREQ            = 0x0b;
    protected static final byte TYPE_AUTO_DAMCON             = 0x0c;
    protected static final byte TYPE_SET_SHIP                = 0x0d;
    protected static final byte TYPE_SET_CONSOLE             = 0x0e;
    protected static final byte TYPE_READY                   = 0x0f;
    protected static final byte TYPE_SCI_SELECT              = 0x10; 
    protected static final byte TYPE_CAPTAIN_SELECT          = 0x11;
    protected static final byte TYPE_GM_SELECT               = 0x12;
    protected static final byte TYPE_SCI_SCAN                = 0x13;
    protected static final byte TYPE_KEYSTROKE               = 0x14;
    protected static final byte TYPE_GM_BUTTON_CLICK         = 0x15;
    protected static final byte TYPE_SHIP_SETUP              = 0x16;

    protected static final byte TYPE_REVERSE_ENGINES         = 0x18;
    protected static final byte TYPE_REQUEST_ENG_GRID_UPDATE = 0x19;
    protected static final byte TYPE_TOGGLE_PERSPECTIVE      = 0x1a;
    protected static final byte TYPE_CLIMB_DIVE              = 0x1b;

    protected static final byte TYPE_LAUNCH_PACKET           = 0x1d;

    private byte mSubType;
    protected int mArg = -1;

    /**
     * Use this constructor if you intend to override writePayload() with your
     * own implementation and not call ShipActionPacket.writePayload().
     * @param subType The desired packet subtype
     */
    public ShipActionPacket(byte subType) {
        super(ConnectionType.CLIENT, TYPE);
        mSubType = subType;
    }

    /**
     * Use this constructor if the packet has a single int argument that is
     * written to the payload after the subtype. In this case, you will not need
     * to override writePayload().
     * @param subType The desired packet subtype
     * @param arg A single argument to write to the payload after the subtype
     */
    public ShipActionPacket(byte subType, int arg) {
        this(subType);
        mArg = arg;
    }

    protected ShipActionPacket(byte subType, PacketReader reader) {
    	this(subType);
    	reader.skip(4); // subtype
    	mArg = reader.readInt();
    }

    @Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mSubType);
		writer.writeInt(mArg);
	}
}