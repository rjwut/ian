package com.walkertribe.ian.protocol.core.eng;

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
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Resets coolant level on all systems to zero. This is sent by the engineering
 * console in response to the user pressing double <kbd>space</kbd> or
 * <kbd>enter</kbd>.
 * @author rjwut
 */
public class EngResetCoolant extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.VALUE_INT;
    private static final SubType SUBTYPE = SubType.ENG_RESET_COOLANT;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return EngResetCoolant.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new EngResetCoolant(reader);
			}
		});
	}

	public EngResetCoolant() {
		super(ConnectionType.CLIENT, TYPE);
	}

	private EngResetCoolant(PacketReader reader) {
		super(ConnectionType.CLIENT, TYPE);
		reader.skip(4); // subtype
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(SUBTYPE.ordinal());
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}
