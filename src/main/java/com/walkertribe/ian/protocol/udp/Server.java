package com.walkertribe.ian.protocol.udp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.walkertribe.ian.iface.ArtemisNetworkInterface;
import com.walkertribe.ian.iface.ThreadedArtemisNetworkInterface;
import com.walkertribe.ian.util.ByteArrayReader;
import com.walkertribe.ian.util.Util;

/**
 * A discovered server
 * @author rjwut
 */
public class Server {
	private String ip;
	private String hostName;

	/**
	 * Converts the data in the given byte array to a Server.
	 */
	static Server from(byte[] bytes) {
		ByteArrayReader reader = new ByteArrayReader(bytes);
		reader.skip(1); // ACK
		return new Server(
				new String(reader.readBytes(reader.readShort()), Util.US_ASCII),
				new String(reader.readBytes(reader.readShort()), Util.US_ASCII)
		);
	}

	Server(String ip, String hostName) {
		if (Util.isBlank(ip)) {
			throw new IllegalArgumentException("You must provide an IP");
		}

		if (Util.isBlank(hostName)) {
			throw new IllegalArgumentException("You must provide a host name");
		}

		this.ip = ip;
		this.hostName = hostName;
	}

	/**
	 * The IP address for this server.
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * The host name for this server.
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * Returns an ArtemisNetworkInterface object that connects to this server.
	 * This method will block until the server responds or an exception is
	 * thrown.
	 */
	public ArtemisNetworkInterface connect(int port) throws IOException {
		return connect(port, 0);
	}

	/**
	 * Returns an ArtemisNetworkInterface object that connects to this server.
	 * This method will block until the server responds, an exception is
	 * thrown, or the timeout elapses.
	 */
	public ArtemisNetworkInterface connect(int port, int timeoutMs) throws IOException {
		return new ThreadedArtemisNetworkInterface(ip, port, timeoutMs);
	}

	/**
	 * Return this data as a byte array to send in response to a UDP request.
	 */
	byte[] toByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] shortBuffer = new byte[2];

		try {
			baos.write(ServerDiscoveryResponder.ACK);
			writeShort(shortBuffer, ip.length(), baos);
			baos.write(ip.getBytes(Util.US_ASCII));
			writeShort(shortBuffer, hostName.length(), baos);
			baos.write(hostName.getBytes(Util.US_ASCII));
			baos.write(0);
			return baos.toByteArray();
		} catch (IOException ex) {
			throw new RuntimeException(ex); // shouldn't happen
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Server)) {
			return false;
		}

		return ip.equals(((Server) obj).ip);
	}

	@Override
	public int hashCode() {
		return ip.hashCode();
	}

	@Override
	public String toString() {
		return hostName + " [" + ip + "]";
	}

	/**
	 * Writes an int (coerced into a short) into the given ByteArrayOutputStream.
	 */
	private static void writeShort(byte[] buffer, int value, ByteArrayOutputStream outStream) {
		buffer[0] = (byte) (0xff & value);
		buffer[1] = (byte) (0xff & (value >> 8));
		outStream.write(buffer, 0, 2);
	}
}
