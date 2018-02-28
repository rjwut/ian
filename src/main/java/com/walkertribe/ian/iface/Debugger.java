package com.walkertribe.ian.iface;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.RawPacket;

/**
 * Interface for objects which can be attached to an ArtemisNetworkInterface to
 * get notified of low-level parsing and writing events.
 * @author rjwut
 */
public interface Debugger {
	/**
	 * Invoked when a packet has been received, but before it is parsed. This
	 * allows you to see the raw payload bytes for each packet as it received.
	 */
	public void onRecvPacketBytes(Origin origin, int pktType, byte[] payload);

	/**
	 * Invoked when a packet is successfully parsed. Packets which are not
	 * parsed (because they are unknown or because no listener is interested in
	 * them) do not trigger this listener.
	 */
	public void onRecvParsedPacket(ArtemisPacket pkt);

	/**
	 * Invoked when a packet is received and not parsed. This may occur because
	 * the packet type is unknown (UnknownPacket), or because no listener is
	 * interested in it (UnparsedPacket).
	 */
	public void onRecvUnparsedPacket(RawPacket pkt);

	/**
	 * Invoked when an exception occurs when attempting to parse a packet.
	 */
	public void onPacketParseException(ArtemisPacketException ex);

	/**
	 * Invoked when an exception occurs when attempting to write a packet.
	 */
	public void onPacketWriteException(ArtemisPacket pkt, Exception ex);

	/**
	 * Invoked just before a packet is written to the PacketWriter.
	 */
	public void onSendPacket(ArtemisPacket pkt);

	/**
	 * Invoked just after a packet is written to the PacketWriter and just
	 * before it is flushed to the OutputStream. This allows you to inspect the
	 * raw payload bytes for each packet as it is sent.
	 */
	public void onSendPacketBytes(Origin origin, int pktType, byte[] payload);

	/**
	 * Invoked when the interface wishes to report a warning.
	 */
	public void warn(String msg);
}