package com.walkertribe.ian.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.ArtemisNetworkInterface;
import com.walkertribe.ian.iface.DisconnectEvent;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.iface.ThreadedArtemisNetworkInterface;
import com.walkertribe.ian.protocol.core.comm.ToggleRedAlertPacket;
import com.walkertribe.ian.world.Artemis;

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
 * </ul>
 * @author rjwut
 */
public class ProxyDemo implements Runnable {
    /**
     * <p>Run with no arguments for usage syntax.</p>
     */
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.out.println("Usage:");
            System.out.println("\tProxyDemo {serverIpOrHostname}[:{port}] [listenerPort]");
            return;
        }

        String serverAddr = args[0];
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 2010;
        new Thread(new ProxyDemo(port, serverAddr)).start();
    }

    private int port;
    private String serverAddr;
    private int serverPort = Artemis.DEFAULT_PORT;

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
            ThreadedArtemisNetworkInterface client = new ThreadedArtemisNetworkInterface(skt, Origin.CLIENT);
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
            server.setAutoSendHeartbeat(false);
            client.setAutoSendHeartbeat(false);
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
