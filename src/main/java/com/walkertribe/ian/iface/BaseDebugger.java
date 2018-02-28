package com.walkertribe.ian.iface;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.RawPacket;

/**
 * A no-op implementation of the Debugger interface. You can extend this class
 * so as to not have to implement all the methods.
 * @author rjwut
 */
public class BaseDebugger implements Debugger {
	@Override
	public void onRecvPacketBytes(Origin origin, int pktType, byte[] payload) {
		// do nothing
	}

	@Override
	public void onRecvParsedPacket(ArtemisPacket pkt) {
		// do nothing
	}

	@Override
	public void onRecvUnparsedPacket(RawPacket pkt) {
		// do nothing
	}

	@Override
	public void onPacketParseException(ArtemisPacketException ex) {
		// do nothing
	}

	@Override
	public void onPacketWriteException(ArtemisPacket pkt, Exception ex) {
		// do nothing
	}

	@Override
	public void onSendPacket(ArtemisPacket pkt) {
		// do nothing
	}

	@Override
	public void onSendPacketBytes(Origin origin, int pktType, byte[] payload) {
		// do nothing
	}

	@Override
	public void warn(String msg) {
		// do nothing
	}
}