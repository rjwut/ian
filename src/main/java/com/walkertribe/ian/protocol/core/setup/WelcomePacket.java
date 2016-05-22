package com.walkertribe.ian.protocol.core.setup;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;

/**
 * Sent by the server immediately on connection. The receipt of this packet
 * indicates a successful connection to the server.
 * @author rjwut
 */
public class WelcomePacket extends BaseArtemisPacket {
	private static final int TYPE = 0x6d04b3da;
	protected static final String MSG = "You have connected to Thom Robertson's Artemis Bridge Simulator.  Please connect with an authorized game client.";

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return WelcomePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new WelcomePacket(reader);
			}
		});
	}

	private String msg = MSG;

	private WelcomePacket(PacketReader reader) {
		super(ConnectionType.SERVER, TYPE);
		msg = reader.readUSASCIIString();
	}

	public WelcomePacket() {
		super(ConnectionType.SERVER, TYPE);
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeUSASCIIString(msg);
	}

	/**
	 * Returns the welcome message sent by the server.
	 */
	public String getMessage() {
		return msg;
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(getMessage());
	}
}