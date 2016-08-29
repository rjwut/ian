Interface for Artemis Networking (IAN)
======================================
**IAN** is an unofficial Java library for communicating with
[Artemis Spaceship Bridge Simulator](http://www.artemis.eochu.com/) servers and clients.

IAN is a heavily revamped version of [ArtClientLib](https://github.com/rjwut/ArtClientLib). It
currently supports Artemis version 2.4.0. If you need Java library support for previous versions of
Artemis, please see the list below:

* Artemis v2.1.1: [ArtClientLib v2.6.0](https://github.com/rjwut/ArtClientLib/releases/tag/v2.6.0)
* Artemis v2.1: [ArtClientLib v2.4.0](https://github.com/rjwut/ArtClientLib/releases/tag/v2.4.0)
* Artemis v2.0: [ArtClientLib v2.3.0](https://github.com/rjwut/ArtClientLib/releases/tag/v2.3.0)
* Artemis v1.x: [ArtClientLib v1.x](https://github.com/dhleong/ArtClientLib) (by Daniel Leong)

This library was originally developed by Daniel Leong and released on GitHub with permission of the
developer of Artemis, Thom Robertson.

## Things to Know Before Using This Library ##

**IAN is an unofficial library.** Thom Robertson and Incandescent Workshop are not involved with its
development and do not provide support for it. Please don't bother them with questions about it.

**Stuff might not work.** There is no official public documentation of the Artemis protocol. A
library such as IAN is made possible through reverse engineering, requiring careful observation of
Artemis network traffic and experimentation to learn how game data is formatted. IAN is a very good
implementation of the Artemis protocol, but under these circumstances it is next to impossible to
guarantee that it is 100% accurate. Also, new Artemis releases are likely to break this library
until a contributor can figure out what changes were made and update things accordingly.

**There is no official support.**
[Bug reports and feature requests](https://github.com/rjwut/ian/issues) are welcome, and I'm happy
to try to answer questions. However, IAN is developed in my spare time, and as such, I can't
guarantee support for it. I can't make any promises to address your requests, bug reports or
questions in a timely fashion.

**Use at your own risk.** This software is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; not even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.

**Don't pirate Artemis.** IAN is intended to allow you to enhance your Artemis experience with
custom behavior. However, using a custom Artemis client or server proxy does not release you from
the obligation to license the Artemis software. If you are playing Artemis without a license, either
with stock or custom software, you are engaging in software piracy and inhibiting the future
development of Artemis. If you don't have a license, please support Thom by purchasing one now.

**I'd love to see what you do with it!** If you make something cool from this, I'd love to know.
Crediting this library would be appreciated, as would sharing any improvements you make, to
potentially include upstream contributions in the form of pull requests.

**You can help make IAN better.** There are several ways that you can assist in IAN's development:
* Found a bug or missing feature?
  [Check to see if I already know about it.](https://github.com/rjwut/ian/issues) If not, you can
  [submit a new issue](https://github.com/rjwut/ian/issues/new).
* Handy with a packet sniffer? Join in the effort to
  [document the Artemis protocol](https://github.com/artemis-nerds/protocol-docs).
* Are you a Java developer? Feel free to implement a feature or bug fix yourself! Fork the
  repository, apply the changes to your copy, then submit a pull request.

## Using IAN ##

### Connecting to an Artemis Server ###
1.  **Tell IAN where to find Artemis resource files.** There are three kinds of Artemis resources
    that IAN may need to load:

    - `dat/vesselData.xml` (required)
    - `dat/*.dxs` (loaded if you request a `Model` from a `Vessel`)
    - `dat/*.snt` (loaded if you request a `VesselInternals` from a `Vessel`)

    In order to do this, IAN needs to know where these resources are located. There is an interface
    called `PathResolver` which is for objects which can locate a resource from a given path
    relative to an Artemis. The `VesselData.setPathResolver()` static method allows you to provide
    IAN with a `PathResolver` to use to load resources. IAN comes with two implementations of
    `PathResolver` out of the box:

    - `FilePathResolver`: Loads resources from the file system relative to a particular directory
      (the Artemis install directory, most likely).
    - `ClasspathResolver`: Loads resources from the classpath (so that you can bundle them inside
      your JAR).

    If one of these implementations is not suitable for your needs, you can of course implement
    `PathResolver` yourself. It should be noted that IAN does not require a full Artemis install; at
    most only the three types of files listed above are needed.

2.  **Construct a `ThreadedArtemisNetworkInterface` object.** This object is responsible for
    managing the connection to the Artemis server. You must provide the constructor with the host/IP
    address and port to which it should connect. (By default, Artemis servers listen for connections
    on port 2010, but this can be changed in the artemis.ini file.) On construction, it will attempt
    to connect, throwing an `IOException` if it fails.

3.  **Add event listeners.** Next, you must add one or more event listeners to the
    `ThreadedArtemisNetworkInterface` object via the `addListener()` method. Event listeners are
    objects which IAN will notify when certain events occur. An event listener can be any `Object`
    which has one or more methods marked with the `@Listener` annotation. A listener method must be
    `public`, return `void`, and have exactly one argument of type `ConnectionEvent`,
    `ArtemisPacket`, `ArtemisObject`, or any of their subtypes. Listener methods are invoked by IAN
    when the corresponding event occurs. For example, if you create a listener method whose argument
    type is `CommsIncomingPacket`, that method will be invoked every time the comms console receives
    a text message.

    One important event to listen for is the `ConnectionSuccessEvent`. You shouldn't attempt to send
    any packets to the server before you receive this event. This may also a good time to send a
    `SetShipPacket` and a `SetConsolePacket`.

    It's also likely that you'll want to know when the connection to the server is lost; listening
    for `DisconnectEvent` will handle that.

4.  **Start the network interface.** Once your listeners are registered, invoke `start()` on the
    `ThreadedArtemisNetworkInterface` object. Internally, this spins up two `Thread`s, one to
    receive and parse incoming packets, and one to send packets. Once you invoke `start()`, your
    listeners will start being notified of events.

5.  **Send packets in response to events.** As your listeners receive events, you will inevitably
    want to send packets back to the Artemis server. To do so, construct an instance of the
    appropriate subclass of `ArtemisPacket`, then pass it into the
    `ThreadedArtemisNetworkInterface.send()` method. For example, to fire a torpedo, construct a
    `FireTubePacket` and `send()` it.

6.  **Disconnect.** To disconnect from the Artemis server, invoke the
    `ThreadedArtemisNetworkInterface.stop()` method. Make sure you do this so that the send and
    receive threads will be terminated; otherwise, your application won't stop.

### Example Client ###
```java
package com.walkertribe.ian.clientdemo;

import java.io.IOException;

import com.walkertribe.ian.enums.AlertStatus;
import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.iface.ArtemisNetworkInterface;
import com.walkertribe.ian.iface.ConnectionSuccessEvent;
import com.walkertribe.ian.iface.DisconnectEvent;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.iface.ThreadedArtemisNetworkInterface;
import com.walkertribe.ian.protocol.core.GameOverPacket;
import com.walkertribe.ian.protocol.core.comm.ToggleRedAlertPacket;
import com.walkertribe.ian.protocol.core.setup.ReadyPacket;
import com.walkertribe.ian.protocol.core.setup.SetConsolePacket;
import com.walkertribe.ian.protocol.core.setup.SetShipPacket;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.PlayerShipUpdateListener;
import com.walkertribe.ian.vesseldata.FilePathResolver;
import com.walkertribe.ian.vesseldata.VesselData;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * <p>
 * This is an example Artemis client which will toggle red alert whenever the
 * shields are raised or lowered. You can use this class as a starting point for
 * your own client.
 * </p>
 * <p>
 * It also demonstrates the use of the PlayerShipUpdateListener class, which is
 * convenient for listening for updates to a player ship by number.
 * </p>
 * @author rjwut
 */
public class ClientDemo extends PlayerShipUpdateListener {
	private static final int DEFAULT_ARTEMIS_PORT = 2010;

	/**
	 * <p>
	 * Usage:
	 * </p>
	 * <code>ClientDemo {ipOrHostname}\[:{port}] {artemisInstallPath} [shipNumber]</code>
	 * <p>
	 * If omitted, the port is assumed to be 2010 and the ship number is assumed
	 * to be 1.
	 * </p>
	 */
	public static void main(String[] args) {
		if (args.length < 2 || args.length > 3) {
			System.out.println(
					"Usage: ClientDemo {ipOrHostname}\[:{port}] {artemisInstallPath} [shipNumber]"
			);
			return;
		}

		int shipNumber = args.length == 3 ? Integer.parseInt(args[2]) : 1;

		try {
			new ClientDemo(args[0], args[1], shipNumber);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private ArtemisNetworkInterface server;
	private boolean redAlert = false;
	private boolean shieldsUp = false;

	/**
	 * Starts the client and connects to the server.
	 */
	public ClientDemo(String host, String artemisInstallPath, int shipNumber) throws IOException {
		super(shipNumber);
		System.out.println("Connecting to " + host + "...");
		int port = DEFAULT_ARTEMIS_PORT;
		int colonPos = host.indexOf(':');

		if (colonPos != -1) {
			port = Integer.parseInt(host.substring(colonPos + 1));
			host = host.substring(0, colonPos);
		}

		VesselData.setPathResolver(new FilePathResolver(artemisInstallPath));
		server = new ThreadedArtemisNetworkInterface(host, port);
		server.addListener(this);
		server.start();
		System.out.println("Connected!");
	}

	/**
	 * We've successfully connected to the server. Select the observer console
	 * on the desired ship and signal our readiness.
	 */
	@Listener
	public void onConnectSuccess(ConnectionSuccessEvent event) {
		server.send(new SetShipPacket(getNumber()));
		server.send(new SetConsolePacket(Console.OBSERVER, true));
		server.send(new ReadyPacket());
		System.out.println("Selected observer console on ship #" + getNumber());
	}

	/**
	 * The connection to the server has been lost. Print a notification to the
	 * console. If the disconnection appears to be abnormal and we have a stack
	 * trace, print it out. Note that the Listener annotation is not needed
	 * because it is inherited from the superclass.
	 */
	@Override
	public void onDisconnect(DisconnectEvent event) {
		super.onDisconnect(event);
		System.out.println(event);
		Exception ex = event.getException();

		if (!event.isNormal() && ex != null) {
			ex.printStackTrace();
		}
	}

	/**
	 * Listens for updates to ArtemisPlayer objects. If it finds one, then it
	 * will check the shield and alert status, then toggle red alert if needed.
	 * Note that this method does not have a Listener annotation, because the
	 * superclass has a listener method called
	 * <code>onPlayerObjectUpdated(ArtemisPlayer)</code> that invokes this
	 * method.
	 */
	@Override
	public void onShipUpdate(ArtemisPlayer player) {
		// Update the current alert status
		AlertStatus alert = player.getAlertStatus();

		if (alert != null) {
			redAlert = AlertStatus.RED.equals(alert);
		}

		// Update shield status
		BoolState shields = player.getShieldsState();

		if (BoolState.isKnown(shields)) {
			shieldsUp = shields.getBooleanValue();
		}

		// Toggle alert state if needed
		if (shieldsUp && !redAlert || !shieldsUp && redAlert) {
			server.send(new ToggleRedAlertPacket());
		}
	}

	/**
	 * The game is over; reset the redAlert and shieldsUp flags. Note that the
	 * Listener annotation is not needed because it is inherited from the
	 * superclass.
	 */
	@Override
	public void onGameOver(GameOverPacket pkt) {
		redAlert = false;
		shieldsUp = false;
	}
}
```

### Creating an Artemis Proxy Server ###
1.  **Tell IAN where to find Artemis resource files.** This is done in exactly the same way as when
    when you create a client.

1.  **Listen for a client connection.**
    Open a `ServerSocket` on the desired port, then call `accept()` on it to listen for a connecting
    client. The `accept()` method will block until a client connects or it times out, and return a
    `Socket` object when the client connects. You can set the timeout by calling
    `ServerSocket.setSoTimeout()`; passing in `0` will cause it to wait indefinitely for a
    connection.

2.  **Wrap the client `Socket` in a `ThreadedArtemisNetworkInterface` object.**
    `ThreadedArtemisNetworkInterface` has a constructor that accepts a `Socket` and a
    `ConnectionType` (`CLIENT` in this case). The resulting object will be responsible for managing
    the connection to the client.

3.  **Connect to the Artemis server.** This is done exactly the same way as you would for creating
    an Artemis client, as documented above. You now have two `ThreadedArtemisNetworkInterface`
    objects: one for the client and one for the server.

4.  **Pass through all packets.** The `proxyTo()` method on `ThreadedArtemisNetworkInterface`
    creates a "pass through" connection between two connections of opposite types. Any packet that
    is not caught by a listener method will be passed through automatically. Note that `proxyTo()`
    only creates connection in one direction; to pass through packets both ways, each connection
    will need to call `proxyTo()` on the other.

5.  **Add listeners.** Add your listeners to both the client and server objects. Once the listener
    has caught the packet and extracted whatever information it wants from it, you can either pass
    the packet along by passing it to `send()` (potentially modifying it first), suppress it (by
    doing nothing), or even inject your own packets instead (by constructing them and passing them
    to `send()`). Remember that `proxyTo()` does **not** pass along packets caught by listeners, so
    it's up to you to `send()` them if you want them passed along. Also, keep in mind that multiple
    listeners can catch the same packet; be careful not to send it more than once!

6.  **When one side disconnects, close the connection to the other side.** Listen for the
    `DisconnectEvent` from both sides. When you receive it from one side, invoke
    `ThreadedArtemisNetworkInterface.stop()` on the other connection (or both connections, if that's
    easier; calling `stop()` on an already closed connection has no effect).

### Example Proxy ###
```java
package com.walkertribe.ian.proxydemo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.ArtemisNetworkInterface;
import com.walkertribe.ian.iface.DisconnectEvent;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.iface.ThreadedArtemisNetworkInterface;
import com.walkertribe.ian.protocol.core.comm.ToggleRedAlertPacket;
import com.walkertribe.ian.vesseldata.FilePathResolver;
import com.walkertribe.ian.vesseldata.VesselData;

/**
 * <p>
 * This is an example Artemis proxy which disables the client's red alert toggle
 * function. You can use this class as a starting point for your own proxy.
 * </p>
 * <p>
 * A proxy listens on a particular port for a client to connect. Upon receiving
 * a connection, the proxy will then connect to the server and will pass packets
 * between them. The client believes that the proxy is the real server, and the
 * server believes that the proxy is the real client. This allows the proxy to
 * be a "man in the middle" and inspect any packet. It can also, if it chooses,
 * modify or suppress packets, or inject new ones.
 * </p>
 * <p>
 * This is a very simple proxy. There are some additional things to consider:
 * </p>
 * <ul>
 * <li>
 *   This class only accepts a single client connection. Once the client
 *   connects, the proxy stops listening for more connections. If you wish to
 *   accept multiple client connections, you can simply spin up a new proxy each
 *   time a client connects.
 * </li>
 * <li>
 *   If not all clients will be connecting to the same server, you will need
 *   some mechanism for determining which server to connect to. For example,
 *   you could assign a different listener port for each server, and spin up a
 *   proxy for each port.
 * </li>
 * <li>
 *   The proxyTo() method will only automatically pass through packets that
 *   aren't caught by any listener method. If you want a packet that is
 *   processed by a listener method to be passed through, you will need to do so
 *   yourself (by passing it to the send() method on the opposite connection).
 *   Note that it is possible for the same packet to be caught by more than one
 *   listener method; be careful not to send the same packet more than once.
 * </li>
 * </p>
 * @author rjwut
 */
public class ProxyDemo implements Runnable {
	/**
	 * <p>
	 * Usage:
	 * </p>
	 * <code>ProxyDemo {artemisInstallPath} {serverIpOrHostname}\[:{port}] [listenerPort]</code>
	 * <p>
	 * The default Artemis port (2010) is assumed where unspecified.
	 * </p>
	 */
	public static void main(String[] args) {
		if (args.length < 2 || args.length > 3) {
			System.out.println("Usage:");
			System.out.println("\tProxyDemo {artemisInstallPath} {serverIpOrHostname}\[:{port}] [listenerPort]");
			return;
		}

		VesselData.setPathResolver(new FilePathResolver(args[0]));
		String serverAddr = args[1];
		int port = args.length > 1 ? Integer.parseInt(args[2]) : 2010;
		new Thread(new ProxyDemo(port, serverAddr)).start();
	}

	private int port;
	private String serverAddr;
	private int serverPort;

	/**
	 * Creates a new proxy. It will listen on the given port, and connect to the
	 * server at the indicated address when it receives a client connection.
	 * After construction, you can start the proxy by spinning it up on a
	 * thread.
	 */
	public ProxyDemo(int port, String serverAddr) {
		this.port = port;
		int colonPos = serverAddr.indexOf(':');

		if (colonPos == -1) {
			this.serverAddr = serverAddr;
		} else {
			this.serverAddr = serverAddr.substring(0, colonPos);
			serverPort = Integer.parseInt(serverAddr.substring(colonPos + 1));
		}
	}

	/**
	 * Starts the proxy. The proxy will not begin listening for a client until
	 * this method runs.
	 */
	@Override
	public void run() {
		ServerSocket listener = null;

		try {
			// Listen for a client connection
			listener = new ServerSocket(this.port, 0);
			listener.setSoTimeout(0);
			System.out.println("Listening for connections on port " + this.port + "...");
			Socket skt = listener.accept();

			// We've got a connection, build interfaces and listener
			System.out.println("Received connection from " + skt.getRemoteSocketAddress());
			ThreadedArtemisNetworkInterface client = new ThreadedArtemisNetworkInterface(skt, ConnectionType.CLIENT);
			System.out.println("Connecting to server at " + serverAddr + ":" + serverPort + "...");
			ThreadedArtemisNetworkInterface server = new ThreadedArtemisNetworkInterface(serverAddr, serverPort, 2000);
			new ProxyListener(server, client);
			System.out.println("Connection established.");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close connections if we get an exception
			if (listener != null && !listener.isClosed()) {
				try {
					listener.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Class which manages the bridge between the server and client and holds
	 * listener methods.
	 * @author rjwut
	 */
	public class ProxyListener {
		private ArtemisNetworkInterface server;
		private ArtemisNetworkInterface client;

		/**
		 * Adds this object as a listener on both the client and the server,
		 * then starts listening to both.
		 */
		private ProxyListener(ArtemisNetworkInterface server,
				ArtemisNetworkInterface client) {
			this.server = server;
			this.client = client;
			client.addListener(this); // we're only listening to client packets
			server.proxyTo(client);
			client.proxyTo(server);
			server.start();
			client.start();
		}

		/**
		 * If one connection is closed, close the other. (Calling stop() on a
		 * connection which is already closed has no effect, so it's easiest to
		 * just call stop() on both.)
		 */
		@Listener
		public void onDisconnect(DisconnectEvent event) {
			server.stop();
			client.stop();
			System.out.println("Disconnect: " + event);

			if (!event.isNormal() && event.getException() != null) {
				// Abnormal termination, print a stack trace
				event.getException().printStackTrace();
			}
		}

		/**
		 * This method "swallows" ToggleRedAlertPackets. This works because IAN
		 * will only automatically pass along packets which are not caught by
		 * listener methods. Any listener method that wants the packet passed
		 * along must do so manually. This method does not, so the packet is
		 * intercepted by the proxy and the server never receives it.
		 */
		@Listener
		public void onPacket(ToggleRedAlertPacket pkt) {
			System.out.println("Intercepted red alert toggle!");
		}
	}
}
```

### Tracking World State ###
The most frequently received packets from the server are ones which provide updates on the status of
objects in the game world. These updates typically contain only that information which has changed,
not the complete state of the object. The `SystemManager` class aggregates the received updates in
order to provide an up-to-the-moment view of the game world. To use it, simply construct a new
`SystemManager` and add it as a listener to your `ThreadedArtemisNetworkInterface`; it has several
listener methods which collect the relevant packets to build the game world. You can then use the
various `get*()` methods to retrieve game state from the `SystemManager`.

### Reading `vesselData.xml` ###
The `VesselData` class allows you to access the information about the vessels and factions in the
game. Note that you must invoke `VesselData.setPathResolver()` as noted above before you attempt to
access any data in the `VesselData` class or connect to other machines.

Invoke the `VesselData.get()` static method to get the `VesselData` instance. This is a singleton
that exposes the information in the `vesselData.xml` file. From there, you can invoke
`getFaction(int)` or `getVessel(int)` to retrieve the `Faction` or `Vessel` with the corresponding
ID, or use the `factionIterator()` or `vesselIterator()` methods to see all of them.

Resource files are loaded on demand (when you request a `Model` or `VesselInternals` object). If you
wish to preload resources up front, you can do so with the `preloadModels()` and
`preloadInternals()` methods.

You should make sure that your code deals gracefully with the possibility that no `Faction` or
`Vessel` with a given ID exists (in which case, the `get*()` methods will return null). For example,
if your `vesselData.xml` file doesn't match the server's, this can occur.

## Acknowledgements ##
Thanks to Thom Robertson and Incandescent Workshop for creating Artemis, and for so graciously
tolerating the community's desire to tinker with their baby.

Daniel Leong ([@dhleong](https://github.com/dhleong)) created the original version of ArtClientLib
and did a lot of the difficult, thankless gruntwork of reverse engineering the Artemis protocol.

Various GitHub users have contributed to IAN by helping to discover and document the protocol,
creating pull requests to implement features or bug fixes, or issuing bug reports. Their help is
very much appreciated. They are, in alphabetical order:
* [@briandurney](https://github.com/briandurney)
* [@dthaler](https://github.com/dthaler)
* [@huin](https://github.com/huin)
* [@IvanSanchez](https://github.com/IvanSanchez)
* [@karafelix](https://github.com/karafelix)
* [@kiwi13cubed](https://github.com/kiwi13cubed)
* [@mrfishie](https://github.com/mrfishie)
* [@prophile](https://github.com/prophile)
* [@russjudge](https://github.com/russjudge)
* [@theGuyFromHell](https://github.com/theGuyFromHell)
