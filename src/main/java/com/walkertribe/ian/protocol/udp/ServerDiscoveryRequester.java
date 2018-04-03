package com.walkertribe.ian.protocol.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Searches for servers on the LAN. When run on a Thread, the requester will
 * broadcast out a request to discover servers, then listen for response for a
 * configurable amount of time. The requester can be reused to send out
 * requests again.
 * @author rjwut
 */
public class ServerDiscoveryRequester implements Runnable {
	static final byte ENQ = 0x05;
	private static final byte[] DATA = new byte[] { ENQ };

	/**
	 * Interface for an object which is notified when a server is discovered or
	 * the discovery process ends.
	 */
	public interface Listener {
		/**
		 * Invoked when a server is discovered.
		 */
		public void onDiscovered(Server server);

		/**
		 * Invoked with the ServerDiscoveryRequester stops listening for responses.
		 */
		public void onStop();
	}

	private Listener listener;
	private int timeoutMs;
	private DatagramSocket skt;
	private InetAddress defaultBroadcastAddr;
	private byte[] buffer = new byte[255];

	/**
	 * Sets up a new ServerDiscoveryRequester that will notified the given
	 * Listener for each server that is discovered. The requester will
	 * listen for responses for the indicated amount of time, then shut
	 * down.
	 */
	public ServerDiscoveryRequester(Listener listener, int timeoutMs) throws UnknownHostException {
		if (listener == null) {
			throw new IllegalArgumentException("You must provide a listener");
		}

		if (timeoutMs < 1) {
			throw new IllegalArgumentException("Invalid timeout: " + timeoutMs);
		}

		this.listener = listener;
		this.timeoutMs = timeoutMs;
		defaultBroadcastAddr = InetAddress.getByName("255.255.255.255");
	}

	@Override
	public synchronized void run() {
		try {
			skt = new DatagramSocket();
			skt.setBroadcast(true);
			skt.send(new DatagramPacket(DATA, 1, defaultBroadcastAddr, ServerDiscoveryResponder.PORT));
			long endTime = System.currentTimeMillis() + timeoutMs;

			do {
				skt.setSoTimeout(Math.max((int) (endTime - System.currentTimeMillis()), 1));
				DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);

				try {
					skt.receive(pkt);
					byte[] data = pkt.getData();

					if (data[0] != ServerDiscoveryResponder.ACK) {
						continue; // didn't get ACK; ignore
					}

					listener.onDiscovered(Server.from(pkt.getData()));
				} catch (SocketTimeoutException ex) {
					break;
				}
			} while (true);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			skt.close();
			skt = null;
			listener.onStop();
		}
	}
}
