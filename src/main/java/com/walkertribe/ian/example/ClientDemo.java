package com.walkertribe.ian.example;

import java.io.IOException;

import com.walkertribe.ian.enums.AlertStatus;
import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.iface.ArtemisNetworkInterface;
import com.walkertribe.ian.iface.ConnectionSuccessEvent;
import com.walkertribe.ian.iface.DisconnectEvent;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.iface.ThreadedArtemisNetworkInterface;
import com.walkertribe.ian.protocol.core.EndGamePacket;
import com.walkertribe.ian.protocol.core.comm.ToggleRedAlertPacket;
import com.walkertribe.ian.protocol.core.setup.ReadyPacket;
import com.walkertribe.ian.protocol.core.setup.SetConsolePacket;
import com.walkertribe.ian.protocol.core.setup.SetShipPacket;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.PlayerShipUpdateListener;
import com.walkertribe.ian.world.Artemis;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * <p>
 * This is an example Artemis client which will toggle red alert whenever the
 * shields are raised or lowered. You can use this class as a starting point for
 * your own client.
 * </p>
 * <p>
 * It also demonstrates the use of the PlayerShipUpdateListener class, which is
 * convenient for listening for updates to a player ship by index.
 * </p>
 * @author rjwut
 */
public class ClientDemo extends PlayerShipUpdateListener {
    /**
     * <p>Run with no arguments for usage syntax.</p>
     */
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.out.println(
                    "Usage: ClientDemo {ipOrHostname}[:{port}] [shipIndex]"
            );
            return;
        }

        byte shipIndex = args.length == 2 ? Byte.parseByte(args[1]) : 0;

        try {
            new ClientDemo(args[0], shipIndex);
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
    public ClientDemo(String host, byte shipIndex) throws IOException {
        super(shipIndex);
        System.out.println("Connecting to " + host + "...");
        int port = Artemis.DEFAULT_PORT;
        int colonPos = host.indexOf(':');

        if (colonPos != -1) {
            port = Integer.parseInt(host.substring(colonPos + 1));
            host = host.substring(0, colonPos);
        }

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
        server.send(new SetShipPacket(getIndex()));
        server.send(new SetConsolePacket(Console.OBSERVER, true));
        server.send(new ReadyPacket());
        System.out.println("Selected observer console on ship #" + getIndex());
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
    public void onGameOver(EndGamePacket pkt) {
        redAlert = false;
        shieldsUp = false;
    }
}
