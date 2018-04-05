package com.walkertribe.ian.example;

import java.io.IOException;

import com.walkertribe.ian.protocol.udp.ServerDiscoveryResponder;

/**
 * <p>
 * This is an example showing how to make your server discoverable on the LAN.
 * While it is running, it will respond to discovery requests from clients.
 * This demo runs indefinitely until killed; in your code, you'll want to
 * invoke stop() on the responder when you don't want to announce your server
 * on the LAN anymore.
 * </p>
 * @author rjwut
 */
public class AnnounceServerDemo {
	public static void main(String[] args) throws IOException {
		final ServerDiscoveryResponder responder = new ServerDiscoveryResponder();
		new Thread(responder).start();
	}
}
