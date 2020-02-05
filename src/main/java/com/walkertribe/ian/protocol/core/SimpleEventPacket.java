package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;

/**
 * A superclass for handling SIMPLE_EVENT server packets.
 * @author rjwut
 */
public abstract class SimpleEventPacket extends BaseArtemisPacket {
    /**
     * SIMPLE_EVENT server packet subtypes.
     */
    public static final class SubType {
    	public static final byte EXPLOSION            = 0x00;
    	public static final byte KLAXON               = 0x01;
    	public static final byte UNKNOWN_02           = 0x02;
    	public static final byte SOUND_EFFECT         = 0x03;
    	public static final byte PAUSE                = 0x04;
    	public static final byte PLAYER_SHIP_DAMAGE   = 0x05;
    	public static final byte END_GAME             = 0x06;
    	public static final byte CLOAK_DECLOAK        = 0x07;
    	public static final byte UNKNOWN_08           = 0x08;
    	public static final byte SKYBOX               = 0x09;
    	public static final byte GAME_MESSAGE         = 0x0a;
    	public static final byte AUTO_DAMCON          = 0x0b;
    	public static final byte JUMP_BEGIN           = 0x0c;
    	public static final byte JUMP_END             = 0x0d;
    	public static final byte UNKNOWN_0E           = 0x0e;
    	public static final byte SHIP_SETTINGS        = 0x0f;
    	public static final byte DMX_MESSAGE          = 0x10;
    	public static final byte KEY_CAPTURE          = 0x11;
    	public static final byte PERSPECTIVE          = 0x12;
    	public static final byte DETONATION           = 0x13;
    	public static final byte GAME_OVER_REASON     = 0x14;
    	public static final byte GAME_OVER_STATS      = 0x15;
    	public static final byte UNKNOWN_16           = 0x16;
    	public static final byte SINGLE_SEAT_LAUNCHED = 0x17;
    	public static final byte SINGLE_SEAT_DAMAGE   = 0x18;
    	public static final byte UNKNOWN_19           = 0x19;
    	public static final byte DOCKED               = 0x1a;
    	public static final byte SMOKE                = 0x1b;
    	public static final byte SINGLE_SEAT_TEXT     = 0x1c;
    	public static final byte TAG                  = 0x1d;
    	public static final byte GAME_OVER            = 0x1e;

    	private SubType() {
    		// prevent instantiation
    	}
    }

    private byte mSubType;

    /**
     * Use this constructor if the class services only one subtype.
     */
    protected SimpleEventPacket() {
        mSubType = getClass().getAnnotation(Packet.class).subtype()[0];
    }

    /**
     * Use this constructor if the class services multiple subtypes.
     */
    protected SimpleEventPacket(byte subType) {
        mSubType = subType;
    }

    protected SimpleEventPacket(PacketReader reader) {
    	mSubType = (byte) reader.readInt();
    }

    @Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mSubType);
	}
}
