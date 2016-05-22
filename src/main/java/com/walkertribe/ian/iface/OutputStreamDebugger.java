package com.walkertribe.ian.iface;

import java.io.OutputStream;
import java.io.PrintStream;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.RawPacket;
import com.walkertribe.ian.util.TextUtil;

/**
 * Debugger implementation that simply writes all packets to an OutputStream.
 * @author rjwut
 */
public class OutputStreamDebugger implements Debugger {
	private String name;
	private PrintStream out;

	/**
	 * Convenience constructor for new OutputStreamDebugger("", System.out);
	 */
	public OutputStreamDebugger() {
		this("", System.out);
	}

	/**
	 * Convenience constructor for new OutputStreamDebugger(name, System.out);
	 */
	public OutputStreamDebugger(String name) {
		this(name, System.out);
	}

	/**
	 * Convenience constructor for new OutputStreamDebugger("", out);
	 */
	public OutputStreamDebugger(OutputStream out) {
		this("", out);
	}

	/**
	 * Creates an OutputStreamDebugger with the indicated name that writes to
	 * the given OutputStream. The name is prefixed to each event that is
	 * written to the stream so you can tell which Debugger is reporting it.
	 */
	public OutputStreamDebugger(String name, OutputStream out) {
		this.name = name;
		this.out = out instanceof PrintStream ? (PrintStream) out : new PrintStream(out);
	}

	@Override
	public void onRecvPacketBytes(ConnectionType connType, int pktType,
			byte[] payload) {
		printPacketBytes(false, connType, pktType, payload);
	}

	@Override
	public void onRecvParsedPacket(ArtemisPacket pkt) {
		out.println(name + "> " + pkt);
	}

	@Override
	public void onRecvUnparsedPacket(RawPacket pkt) {
		out.println(name + "< " + pkt);
	}

	@Override
	public void onSendPacket(ArtemisPacket pkt) {
		out.println(name + "< " + pkt);
	}

	@Override
	public void onSendPacketBytes(ConnectionType connType, int pktType,
			byte[] payload) {
		printPacketBytes(true, connType, pktType, payload);
	}

	@Override
	public void onPacketParseException(ArtemisPacketException ex) {
		System.err.println(ex.getConnectionType() + ": " +
				TextUtil.intToHex(ex.getPacketType()) + " " +
				TextUtil.byteArrayToHexString(ex.getPayload()));
		ex.printStackTrace();
	}

	@Override
	public void onPacketWriteException(ArtemisPacket pkt, Exception ex) {
		System.err.println(pkt);
		ex.printStackTrace();
	}

	@Override
	public void warn(String msg) {
		out.println((name != "" ? (name + ": ") : "") + "WARNING: "  + msg);
	}

	/**
	 * Writes the bytes for the given packet to the OutputStream.
	 */
	private void printPacketBytes(boolean send, ConnectionType connType,
			int pktType, byte[] payload) {
		out.println(
				name + (send ? "< " : "> ") +
				TextUtil.intToHexLE(ArtemisPacket.HEADER) + " " +
				TextUtil.intToHexLE(payload.length + 24) + " " +
				TextUtil.intToHexLE(connType.toInt()) + " " +
				TextUtil.intToHexLE(0) + " " +
				TextUtil.intToHexLE(payload.length + 4) + " " +
				TextUtil.intToHexLE(pktType) + " " +
				TextUtil.byteArrayToHexString(payload)
		);
	}
}