package com.walkertribe.ian.example;

import java.io.IOException;

import com.walkertribe.ian.protocol.udp.ServerDiscoveryResponder;

/**
 * <p>
 * This is an example showing how to make your server discoverable on the LAN.
 * While it is running, it will respond to discovery requests from clients.
 * </p>
 * @author rjwut
 */
public class AnnounceServerDemo {
	public static void main(String[] args) throws IOException {
		final ServerDiscoveryResponder responder = new ServerDiscoveryResponder();
		new Thread(responder).start();
	}
}
