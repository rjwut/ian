package com.walkertribe.ian.protocol.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Respond to server discovery requests.
 * @author rjwut
 */
public class ServerDiscoveryResponder implements Runnable {
	static final int PORT = 3100;
	static final byte ACK = 0x06;

	private DatagramSocket skt;
	private boolean listening = false;
	private byte[] buffer = new byte[1];
	private byte[] response;

	public static void main(String[] args) throws IOException {
		final ServerDiscoveryResponder responder = new ServerDiscoveryResponder();
		new Thread(responder).start();
		ServerDiscoveryRequester.Listener listener = new ServerDiscoveryRequester.Listener() {
			@Override
			public void onDiscovered(Server server) {
				System.out.println("DISCOVERED: " + server);
			}

			@Override
			public void onStop() {
				System.out.println("Stopped");
				responder.stop();
			}
		};
		ServerDiscoveryRequester requester = new ServerDiscoveryRequester(listener, 5000);
		new Thread(requester).start();
	}

	/**
	 * Listen for server discovery requests, guessing the best PrivateNetworkAddress to use.
	 */
	public ServerDiscoveryResponder() throws IOException {
		this(PrivateNetworkAddress.guessBestPrivateNetworkAddress());
	}

	/**
	 * Listen for server discovery requests on the indicated PrivateNetworkAddress.
	 */
	public ServerDiscoveryResponder(PrivateNetworkAddress addr) throws IOException {
		if (addr == null) {
			throw new IllegalArgumentException("You must provide an address");
		}

		skt = new DatagramSocket(PORT);
		skt.setSoTimeout(1000);
		response = new Server(addr.getIp(), addr.getHostName()).toByteArray();
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
