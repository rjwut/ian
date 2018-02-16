package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;

/**
 * Provides intel on another vessel, typically as the result of a level 2 scan.
 * @author rjwut
 */
public class IntelPacket extends BaseArtemisPacket {
	private static final PacketType TYPE = CorePacketType.OBJECT_TEXT;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return IntelPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new IntelPacket(reader);
			}
		});
	}

	private final int mId;
	private final CharSequence mIntel;

	private IntelPacket(PacketReader reader) {
    	super(ConnectionType.SERVER, TYPE);
    	mId = reader.readInt();
    	reader.readUnknown("Unknown", 1);
        mIntel = reader.readString();
    }

	/**
	 * The ID of the ship in question
	 */
	public int getId() {
		return mId;
	}

	/**
	 * The intel on that ship, as human-readable text
	 */
	public CharSequence getIntel() {
		return mIntel;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(mId).writeByte((byte) 3).writeString(mIntel);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("Obj #").append(mId).append(": ").append(mIntel);
	}
}