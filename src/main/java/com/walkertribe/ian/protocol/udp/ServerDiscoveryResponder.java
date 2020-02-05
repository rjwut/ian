package com.walkertribe.ian.protocol.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Respond to server discovery requests. Instantiate this class and spin it up
 * on a Thread to start listening for discovery requests. When you want to stop
 * announcing your server, call stop() on this object and the Thread will
 * (eventually) terminate.
 * @author rjwut
 */
public class ServerDiscoveryResponder implements Runnable {
	static final int PORT = 3100;
	static final byte ACK = 0x06;

	private DatagramSocket skt;
	private boolean listening = false;
	private byte[] buffer = new byte[1];
	private byte[] response;

	/**
	 * Listen for server discovery requests, guessing the best PrivateNetworkAddress to use.
	 */
	public ServerDiscoveryResponder() throws IOException {
		init(PrivateNetworkAddress.guessBest());
	}

	/**
	 * Listen for server discovery requests, reporting the given IP and host name.
	 */
	public ServerDiscoveryResponder(String ip, String hostName) throws IOException {
		init(ip, hostName);
	}

	/**
	 * Initializes the ServerDiscoveryResponder using the given PrivateNetworkAddress.
	 */
	private void init(PrivateNetworkAddress addr) throws IOException {
		if (addr == null) {
			throw new IOException("No suitable network interface found");
		}

		init(addr.getIp(), addr.getHostName());
	}

	/**
	 * Initializes the ServerDiscoveryResponder using the given IP and host name.
	 */
	private void init(String ip, String hostName) throws IOException {
		skt = new DatagramSocket(PORT);
		skt.setSoTimeout(1000);
		response = new Server(ip, hostName).toByteArray();
	}

	@Override
	public void run() {
		listening = true;

		try {
			while (listening) {
				DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);

				try {
					skt.receive(pkt);
					InetAddress address = pkt.getAddress();
					int port = pkt.getPort();

					if (pkt.getData()[0] != ServerDiscoveryRequester.ENQ) {
						continue; // didn't get ENQ; ignore
					}

					DatagramPacket sendPacket = new DatagramPacket(response, response.length, address, port);
					skt.send(sendPacket);
				} catch (SocketTimeoutException ex) {
					// just drop into the next loop
				}
			}
		} catch (SocketException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (skt != null) {
				skt.close();
			}
		}
	}

	/**
	 * Requests this object to stop listening for discovery requests.
	 */
	public void stop() {
		listening = false;
	}
}
