package com.walkertribe.ian.iface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.core.setup.SetShipPacket;
import com.walkertribe.ian.protocol.core.setup.VersionPacket;
import com.walkertribe.ian.protocol.core.setup.WelcomePacket;
import com.walkertribe.ian.util.Version;
import com.walkertribe.ian.world.Artemis;

/**
 * Test that confirms that a server and client interface can connect to one another and exchange
 * packets.
 * @author rjwut
 */
public class ThreadedArtemisNetworkInterfaceTest {
    private static final int CONNECT_TIMEOUT = 1000;
    private static final int DISCONNECT_TIMEOUT = 1000;

    /**
     * Sets up a ServerSocket to listen for a client connection. The client Socket is then stored in
     * the clientSocket property of this object. This runs in a separate Thread so that the main
     * Thread can create the client to connect to it.
     */
    private class ServerStartup implements Runnable {
        private Socket clientSocket;
        private IOException exception;

        @Override
        public void run() {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(Artemis.DEFAULT_PORT);
                serverSocket.setSoTimeout(CONNECT_TIMEOUT);
                clientSocket = serverSocket.accept();
            } catch (IOException ex) {
                exception = ex;
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException ex) {
                        if (exception == null) {
                            exception = ex;
                        }
                    }
                }
            }
        }
    }

    /**
     * Listens for the SetShipPacket from the client and closes the connection in response.
     */
    public static class ServerListener {
        private ArtemisNetworkInterface iface;
        private int shipIndex = -1;

        private ServerListener(ArtemisNetworkInterface iface) {
            this.iface = iface;
        }

        @Listener
        public void onShipSelect(SetShipPacket pkt) {
            shipIndex = pkt.getShipIndex();
            iface.stop();
        }
    }

    /**
     * Listens for the ConnectionSuccessEvent from the server and sends a SetShipPacket in response.
     */
    public static class ClientListener {
        private ArtemisNetworkInterface iface;
        private boolean connected = false;
        private boolean disconnected = false;
        private Exception exception;

        private ClientListener(ArtemisNetworkInterface iface) {
            this.iface = iface;
        }

        @Listener
        public void onConnect(ConnectionSuccessEvent event) {
            connected = true;
            iface.send(new SetShipPacket(0));
        }

        @Listener
        public void onDisconnect(DisconnectEvent event) {
            disconnected = true;

            if (!event.isNormal()) {
                exception = event.getException();
            }
        }
    }

    @Test
    public void test() throws InterruptedException, IOException {
        // Spin up a separate thread where we'll listen for a client connection
        ServerStartup serverStartup = new ServerStartup();
        Thread thread = new Thread(serverStartup);
        thread.start();

        // Create a client and connect to the server
        ArtemisNetworkInterface client = new ThreadedArtemisNetworkInterface("0.0.0.0", Artemis.DEFAULT_PORT);
        ClientListener clientListener = new ClientListener(client);
        client.addListener(clientListener);
        client.start();

        // Wait for connection to be established
        long endTime = System.currentTimeMillis() + CONNECT_TIMEOUT;

        while (thread.isAlive() && endTime > System.currentTimeMillis()) {
            Thread.sleep(10);
        }

        if (thread.isAlive()) {
            client.stop();
            Assert.fail("Connection not established in time");
        }

        // Server has received client connection; wrap it in an ArtemisNetworkInterface and start it
        ArtemisNetworkInterface server = new ThreadedArtemisNetworkInterface(serverStartup.clientSocket, Origin.CLIENT);
        ServerListener serverListener = new ServerListener(server);
        server.addListener(serverListener);
        server.start();

        try {
            // Send WelcomePacket and VersionPacket
            server.send(new WelcomePacket());
            server.send(new VersionPacket(new Version("2.7.4")));
            endTime = System.currentTimeMillis() + DISCONNECT_TIMEOUT;

            // Wait for connection to close
            while (!clientListener.disconnected && endTime > System.currentTimeMillis()) {
                Thread.sleep(10);
            }

            // Assert that all expected events occurred
            if (!clientListener.connected) {
                Assert.fail("Never connected");
            }

            if (serverListener.shipIndex != 0) {
                Assert.fail(serverListener.shipIndex == -1 ? "Did not receive ship index" : "Wrong ship index");
            }

            if (!clientListener.disconnected) {
                Assert.fail("Never disconnected");
            }

            if (clientListener.exception != null) {
                throw new RuntimeException(clientListener.exception);
            }
        } finally {
            server.stop();
            client.stop();
        }
    }
}
