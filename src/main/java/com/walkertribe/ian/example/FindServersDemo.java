package com.walkertribe.ian.example;

import java.io.IOException;

import com.walkertribe.ian.protocol.udp.Server;
import com.walkertribe.ian.protocol.udp.ServerDiscoveryRequester;

/**
 * <p>
 * This is an example showing how to discover servers on the LAN. When you run
 * this demo, it will search for servers for five seconds, printing each one it
 * finds to the console.
 * </p>
 * @author rjwut
 */
public class FindServersDemo implements ServerDiscoveryRequester.Listener {
	public static void main(String[] args) throws IOException {
		FindServersDemo demo = new FindServersDemo();
		ServerDiscoveryRequester requester = new ServerDiscoveryRequester(demo, 5000);
		new Thread(requester).start();
	}

	private int count = 0;

	@Override
	public void onDiscovered(Server server) {
		System.out.println(server);
		count++;
	}

	@Override
	public void onStop() {
		System.out.println("Servers found: " + count);
	}
}
