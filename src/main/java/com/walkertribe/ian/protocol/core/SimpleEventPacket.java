package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;

public abstract class SimpleEventPacket extends BaseArtemisPacket {
    protected static final PacketType TYPE = CorePacketType.SIMPLE_EVENT;

    /**
     * SIMPLE_EVENT server packet subtypes.
     * The order of these enum values matters! Do not reorder!
     */
    public enum SubType {
    	EXPLOSION,            // 00
    	KLAXON,               // 01
    	UNKNOWN_02,           // 02
    	SOUND_EFFECT,         // 03
    	PAUSE,                // 04
    	PLAYER_SHIP_DAMAGE,   // 05
    	GAME_OVER,            // 06
    	CLOAK_DECLOAK,        // 07
    	UNKNOWN_08,           // 08
    	SKYBOX,               // 09
    	GAME_MESSAGE,         // 0a
    	AUTO_DAMCON,          // 0b
    	JUMP_BEGIN,           // 0c
    	JUMP_END,             // 0d
    	UNKNOWN_0E,           // 0e
    	SHIP_SETTINGS,        // 0f
    	DMX_MESSAGE,          // 10
    	KEY_CAPTURE,          // 11
    	PERSPECTIVE,          // 12
    	UNKNOWN_13,           // 13
    	GAME_OVER_REASON,     // 14
    	GAME_OVER_STATS,      // 15
    	UNKNOWN_16,           // 16
    	SINGLE_SEAT_LAUNCHED, // 17
    	SINGLE_SEAT_DAMAGE,   // 18
    	UNKNOWN_19,           // 19
    	DOCKED                // 1a
    }

    private SubType mSubType;

    protected SimpleEventPacket(SubType subType) {
    	super(ConnectionType.SERVER, TYPE);
    	mSubType = subType;
    }

    protected SimpleEventPacket(SubType subType, PacketReader reader) {
    	this(subType);
    	reader.skip(4); // subtype
    }

    @Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mSubType.ordinal());
	}

    /**
     * Convenience method for coercing the SubType enum to a byte.
     */
    protected static void register(PacketFactoryRegistry registry, SubType subType,
    		PacketFactory factory) {
    	registry.register(ConnectionType.SERVER, TYPE, (byte) subType.ordinal(), factory);
    }
}
