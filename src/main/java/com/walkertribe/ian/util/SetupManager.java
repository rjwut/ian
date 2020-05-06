package com.walkertribe.ian.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.Console;
import com.walkertribe.ian.enums.Console.ClaimType;
import com.walkertribe.ian.enums.ConsoleStatus;
import com.walkertribe.ian.enums.DriveType;
import com.walkertribe.ian.iface.ArtemisNetworkInterface;
import com.walkertribe.ian.iface.DisconnectEvent;
import com.walkertribe.ian.iface.HeartbeatLostEvent;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.core.setup.AllShipSettingsPacket;
import com.walkertribe.ian.protocol.core.setup.ConsoleStatusPacket;
import com.walkertribe.ian.protocol.core.setup.SetConsolePacket;
import com.walkertribe.ian.protocol.core.setup.SetShipPacket;
import com.walkertribe.ian.protocol.core.setup.SetShipSettingsPacket;
import com.walkertribe.ian.protocol.core.setup.SetSingleSeatSettingsPacket;
import com.walkertribe.ian.protocol.core.setup.Ship;
import com.walkertribe.ian.protocol.core.setup.SingleSeatCraft;
import com.walkertribe.ian.protocol.core.setup.VersionPacket;
import com.walkertribe.ian.protocol.core.setup.WelcomePacket;
import com.walkertribe.ian.world.Artemis;

/**
 * Tracks the properties of each player ship during setup and which Consoles are taken by which
 * clients, and updates the clients as changes occur.
 * @author rjwut
 */
public class SetupManager {
    private static final Version ARTEMIS_VERSION = new Version("2.7.5");
    private static final String[] DEFAULT_SHIP_NAMES = {
            "Artemis", "Intrepid", "Aegis", "Horatio", "Excalibur", "Hera", "Ceres", "Diana"
    };

    private ShipEntry[] ships = new ShipEntry[Artemis.SHIP_COUNT];
    private Set<Client> clients = new HashSet<>();
    private ShipClaims[] shipClaims = new ShipClaims[Artemis.SHIP_COUNT];
    private Map<Console, Client> gameClaims = new HashMap<>();
    private List<SetupManagerListener> listeners = new LinkedList<>();

    /**
     * Creates a new SetupManager with the default ship configuration and no
     * Consoles claimed.
     */
    public SetupManager(Context ctx) {
        int defaultShipType = ctx.getVesselData().vesselIterator().next().getId();

        for (int i = 0; i < Artemis.SHIP_COUNT; i++) {
            ships[i] = new ShipEntry(DEFAULT_SHIP_NAMES[i], defaultShipType, i / 8f, DriveType.WARP);
            shipClaims[i] = new ShipClaims();
        }

        for (Console console : Console.values()) {
            if (console.getClaimType() == ClaimType.ONE_PER_GAME) {
                gameClaims.put(console, null);
            }
        }
    }

    /**
     * Registers the given object to be notified of SetupManager events.
     */
    public void addSetupManagerListener(SetupManagerListener listener) {
        listeners.add(listener);
    }

    /**
     * Returns the indicated Ship. The returned value is a clone; modifying its properties does not
     * update anything in the SetupManager.
     */
    public Ship getShip(int shipIndex) {
        return new Ship(ships[shipIndex].ship);
    }

    /**
     * Returns the index of the given Ship, or -1 if the given Ship was not found.
     */
    public int getShipIndex(Ship ship) {
        for (int i = 0; i < ships.length; i++) {
            Ship curShip = ships[i].ship;

            if (curShip == ship) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns a List of the SingleSeatCraft for the indicated ship. The returned list is a clone;
     * modifying its properties does not update anything in the SetupManager.
     */
    public List<SingleSeatCraft> getSingleSeatCraft(int shipIndex) {
        return new ArrayList<>(ships[shipIndex].craft);
    }

    /**
     * Returns a Map that describes each ship and its crew.
     */
    public synchronized Map<Ship, List<ArtemisNetworkInterface>> getCrewedShips() {
        Map<Ship, List<ArtemisNetworkInterface>> map = new LinkedHashMap<>();

        for (ShipEntry entry : ships) {
            map.put(entry.ship, new LinkedList<ArtemisNetworkInterface>());
        }

        for (Client client : clients) {
            Ship ship = ships[client.shipIndex].ship;
            map.get(ship).add(client.iface);
        }

        return map;
    }

    /**
     * Invoke this when a new client connects. The client will receive a WelcomePacket, a
     * VersionPacket, an AllShipSettingsPacket, and a ConsoleStatusPacket.
     */
    public void connect(ArtemisNetworkInterface iface) {
        Client client = new Client(iface);
        clients.add(client);
        iface.send(new WelcomePacket());
        iface.send(new VersionPacket(ARTEMIS_VERSION));
        client.send(buildAllShipSettingsPacket());
        client.sendConsolesUpdate();
    }

    /**
     * Returns the index of the given client's assigned ship, or null if it is not assigned.
     */
    public Integer getAssignment(ArtemisNetworkInterface iface) {
        return getClient(iface).shipIndex;
    }

    /**
     * Returns the Consoles claimed by the given client.
     */
    public SortedSet<Console> getConsoles(ArtemisNetworkInterface iface) {
        Client client = getClient(iface);

        if (client == null) {
            return new TreeSet<>();
        }

        SortedSet<Console> consoles = shipClaims[client.shipIndex].getConsoles(client);

        for (Map.Entry<Console, Client> entry : gameClaims.entrySet()) {
            if (entry.getValue() == client) {
                consoles.add(entry.getKey());
            }
        }

        return consoles;
    }

    /**
     * Updates the setup for the indicated Ship and notifies all clients.
     */
    private synchronized void setShip(int shipIndex, Ship ship) {
        ships[shipIndex].update(ship);
        AllShipSettingsPacket pkt = buildAllShipSettingsPacket();

        for (Client client : clients) {
            client.send(pkt);
        }
    }

    /**
     * Constructs a new AllShipSettingsPacket. You don't need to do this to update the clients as
     * they connect or changes to the ships occur, since the SetupManager handles that for you.
     * However, this packet is also sent out when the simulation starts, which SetupManager doesn't
     * do for you.
     */
    public AllShipSettingsPacket buildAllShipSettingsPacket() {
        Ship[] shipArray = new Ship[ships.length];

        for (int i = 0; i < ships.length; i++) {
            shipArray[i] = ships[i].ship;
        }

        return new AllShipSettingsPacket(shipArray);
    }

    /**
     * Invoke this when a client attempts to claim or relinquish a Console. This will check to see
     * if the claim is valid, and if so, register the claim. If the claim is invalid, nothing
     * happens. This returns an Update value indicating which clients should be updated.
     */
    private synchronized Update setClaim(Client client, Console console, boolean on) {
        Update update = Update.SELF;

        if (console.getClaimType() == ClaimType.ONE_PER_GAME) {
            Client owner = gameClaims.get(console);

            if (on && owner == null) {
                gameClaims.put(console, client);
                update = Update.ALL;
                notifyOfClaim(client, console, on);
            } else if (!on && owner == client) {
                gameClaims.put(console, null);
                update = Update.ALL;
                notifyOfClaim(client, console, on);
            }
        } else {
            update = shipClaims[client.shipIndex].setClaim(client, console, on);
        }

        return update;
    }

    /**
     * Unassigns a client from their ship and clears all their claims. This should be invoked
     * if a client disconnects. Returns an Update value that indicates which clients should be
     * updated.
     */
    public synchronized Update forget(Client client) {
        clients.remove(client);
        Update update = removeShipClaims(client);
        return update.stack(removeGameClaims(client));
    }

    /**
     * Removes a client's blocking claims, but leaves any non-blocking claims, and the client
     * remains assigned to their current ship. This should be invoked if a client's heartbeat is
     * lost. Returns an Update value that indicates which clients should be updated.
     */
    private synchronized Update removeSingletonClaims(Client client) {
        Update update = removeSingletonShipClaims(client);
        return update.stack(removeGameClaims(client));
    }

    /**
     * Removes the client's ship claims and returns an Update value indicating which clients should
     * be updated.
     */
    private Update removeShipClaims(Client client) {
        return shipClaims[client.shipIndex].clearClaims(client);
    }

    /**
     * Removes the client's singleton ship claims, and returns an Update value indicating which
     * clients should be updated.
     */
    private Update removeSingletonShipClaims(Client client) {
        return shipClaims[client.shipIndex].clearSingletonClaims(client);
    }

    /**
     * Removes the client's game claims and returns an Update value indicating which clients should
     * be updated.
     */
    private Update removeGameClaims(Client client) {
        Update update = Update.SELF;

        for (Iterator<Client> iter = gameClaims.values().iterator(); iter.hasNext(); ) {
            if (iter.next() == client) {
                iter.remove();
                update = Update.ALL;
            }
        }

        return update;
    }

    /**
     * Returns the Client object that corresponds to the given ArtemisNetworkInterface, or null if
     * no such Client exists.
     */
    private synchronized Client getClient(ArtemisNetworkInterface iface) {
        for (Client client : clients) {
            if (client.iface == iface) {
                return client;
            }
        }

        return null;
    }

    private void notifyOfShipUpdate(Ship ship) {
        for (SetupManagerListener listener : listeners) {
            listener.onShipUpdate(ship);
        }
    }

    private void notifyOfAssignment(Client client) {
        for (SetupManagerListener listener : listeners) {
            listener.onAssignmentUpdate(client.iface, client.shipIndex);
        }
    }

    /**
     * Notifies the claim listeners.
     */
    private void notifyOfClaim(Client client, Console console, boolean on) {
        for (SetupManagerListener listener : listeners) {
            listener.onClaim(client.iface, console, on);
        }
    }

    /**
     * An enum representing the clients which may need a console status update after an event.
     */
    private enum Update {
        /**
         * Update only the client that sent the setup packet.
         */
        SELF {
            @Override
            protected void sendUpdate(Set<Client> clients, Client origin) {
                origin.sendConsolesUpdate();
            }
        },
        /**
         * Update all clients on the same ship as the one that sent the setup packet.
         */
        SHIP {
            @Override
            protected void sendUpdate(Set<Client> clients, Client origin) {
                for (Client client : clients) {
                    if (client.shipIndex == origin.shipIndex) {
                        client.sendConsolesUpdate();
                    }
                }
            }
        },
        /**
         * Update all clients.
         */
        ALL {
            @Override
            protected void sendUpdate(Set<Client> clients, Client origin) {
                for (Client client : clients) {
                    client.sendConsolesUpdate();
                }
            }
        };

        /**
         * Sends a console status update to the clients indicated by this enum value.
         */
        protected abstract void sendUpdate(Set<Client> clients, Client origin);

        public Update stack(Update update) {
            return ordinal() > update.ordinal() ? this : update;
        }
    }

    /**
     * Tracks the ship to which an ArtemisNetworkInterface is assigned.
     */
    public class Client {
        private ArtemisNetworkInterface iface;
        private int shipIndex;

        private Client(ArtemisNetworkInterface iface) {
            this.iface = iface;
            iface.addListener(this);
        }

        /**
         * Sends an ArtemisPacket to this client.
         */
        private void send(ArtemisPacket pkt) {
            iface.send(pkt);
        }

        /**
         * Invoked when the client requests to be assigned to a particular ship.
         */
        @Listener
        public void assign(SetShipPacket pkt) {
            Update update = Update.SELF;
            int newShipIndex = pkt.getShipIndex();

            if (shipIndex != newShipIndex) {
                removeShipClaims(this);
                shipIndex = pkt.getShipIndex();
            }

            update.sendUpdate(clients, this);
            notifyOfAssignment(this);
        }

        /**
         * Invoked when this client changes settings for the ship to which they're assigned.
         */
        @Listener
        public void onShipSettingsUpdate(SetShipSettingsPacket pkt) {
            Ship ship = pkt.getShip();
            setShip(shipIndex, ship);
            notifyOfShipUpdate(ship);
        }

        /**
         * Invoked when the client attempts to claim (or clear its claim for) a console.
         */
        @Listener
        public void setClaim(SetConsolePacket pkt) {
            Update update = SetupManager.this.setClaim(this, pkt.getConsole(), pkt.isSelected());
            update.sendUpdate(clients, this);
        }

        /**
         * Invoked when the client updates the settings for single seat craft.
         */
        @Listener
        public void setSingleSeatCraftUpdate(SetSingleSeatSettingsPacket pkt) {
            ships[shipIndex].update(pkt);
        }

        /**
         * Invoked when the client's heartbeat is lost. Any of its singleton claims will be cleared.
         */
        @Listener
        public void onHeartbeatLost(HeartbeatLostEvent e) {
            removeSingletonClaims(this).sendUpdate(clients, this);
        }

        /**
         * Invoked when the client is disconnected. Causes the SetupManager to forget the client and
         * clear all of its claims.
         */
        @Listener
        public void onDisconnect(DisconnectEvent e) {
            forget(this).sendUpdate(clients, this);
        }

        /**
         * Sends a ConsoleStatusPacket to this client.
         */
        private synchronized void sendConsolesUpdate() {
            Console[] consoles = Console.values();
            ConsoleStatus[] statuses = new ConsoleStatus[consoles.length];
            ShipClaims curShipClaims = shipClaims[shipIndex];

            for (int i = 0; i < consoles.length; i++) {
                Console console = consoles[i];
                ConsoleStatus status;

                if (console.getClaimType() == ClaimType.ONE_PER_GAME) {
                    Client claimedBy = gameClaims.get(console);
                    
                    if (claimedBy == null) {
                        status = ConsoleStatus.AVAILABLE;
                    } else if (claimedBy == this) {
                        status = ConsoleStatus.YOURS;
                    } else {
                        status = ConsoleStatus.UNAVAILABLE;
                    }
                } else {
                    status = curShipClaims.getStatus(console, this);
                }

                statuses[i] = status;
            }

            send(new ConsoleStatusPacket(shipIndex, statuses));
        }
    }

    private class ShipEntry {
        private Ship ship;
        private List<SingleSeatCraft> craft;

        private ShipEntry(CharSequence name, int shipType, float accentColor, DriveType drive) {
            this.ship = new Ship(name, shipType, accentColor, drive);
        }

        private void update(Ship ship) {
            this.ship = ship;
        }

        private void update (SetSingleSeatSettingsPacket pkt) {
            craft = pkt.getCraftList();
        }
    }

    /**
     * Tracks claims for a single ship.
     */
    private class ShipClaims {
        private Map<Console, Client> singletonClaims = new HashMap<>();
        private Map<Console, Set<Client>> multiClaims = new HashMap<>();

        private ShipClaims() {
            for (Console console : Console.values()) {
                ClaimType claimType = console.getClaimType();

                if (claimType == ClaimType.ONE_PER_SHIP) {
                    singletonClaims.put(console, null);
                } else if (claimType == ClaimType.NO_LIMIT) {
                    multiClaims.put(console, new HashSet<Client>());
                }
            }
        }

        /**
         * Returns the Consoles the given client has claimed on this ship.
         */
        private SortedSet<Console> getConsoles(Client client) {
            SortedSet<Console> consoles = new TreeSet<>();

            for (Map.Entry<Console, Client> entry : singletonClaims.entrySet()) {
                if (entry.getValue() == client) {
                    consoles.add(entry.getKey());
                }
            }

            for (Map.Entry<Console, Set<Client>> entry : multiClaims.entrySet()) {
                if (entry.getValue().contains(client)) {
                    consoles.add(entry.getKey());
                }
            }

            return consoles;
        }

        /**
         * Attempts to set the claim for the indicated console on this ship. If the claim is
         * invalid, nothing happens. This method returns an Update value indicating which clients
         * should be updated.
         */
        private Update setClaim(Client client, Console console, boolean on) {
            Update update = Update.SELF;

            if (console.getClaimType() == ClaimType.ONE_PER_SHIP) {
                Client currentClaim = singletonClaims.get(console);

                if (on && currentClaim == null) {
                    singletonClaims.put(console, client);
                    update = Update.SHIP;
                    notifyOfClaim(client, console, on);
                } else if (!on && currentClaim == client) {
                    singletonClaims.put(console, null);
                    update = Update.SHIP;
                    notifyOfClaim(client, console, on);
                }
            } else {
                Set<Client> claims = multiClaims.get(console);

                if (on ? claims.add(client) : claims.remove(client)) {
                    notifyOfClaim(client, console, on);
                }
            }

            return update;
        }

        /**
         * Removes all claims from the indicated client for this ship and returns an Update value
         * indicating which clients should be updated.
         */
        private Update clearClaims(Client client) {
            for (Map.Entry<Console, Set<Client>> entry : multiClaims.entrySet()) {
                if (entry.getValue().remove(client)) {
                    notifyOfClaim(client, entry.getKey(), false);
                }
            }

            return clearSingletonClaims(client);
        }

        /**
         * Removes all singleton claims from the indicated client for this ship and returns an
         * Update value indicating which clients should be updated.
         */
        private Update clearSingletonClaims(Client client) {
            Update update = Update.SELF;

            for (Iterator<Entry<Console, Client>> iter = singletonClaims.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry<Console, Client> entry = iter.next();

                if (entry.getValue() == client) {
                    iter.remove();
                    update = Update.SHIP;
                    notifyOfClaim(client, entry.getKey(), false);
                }
            }

            return update;
        }

        /**
         * Returns the status of the named Console for the given client on this ship.
         */
        private ConsoleStatus getStatus(Console console, Client client) {
            Client owner;

            if (console.getClaimType() == ClaimType.ONE_PER_SHIP) {
                owner = singletonClaims.get(console);
            } else {
                owner = multiClaims.get(console).contains(client) ? client: null;
            }

            if (owner == client) {
                return ConsoleStatus.YOURS;
            }

            return owner == null ? ConsoleStatus.AVAILABLE : ConsoleStatus.UNAVAILABLE;
        }
    }

    /**
     * Interface for objects which want to be informed of SetupManager events. These events are
     * guaranteed to fire AFTER the SetupManager is updated.
     */
    public interface SetupManagerListener {
        /**
         * Invoked when a Ship's setup is updated.
         */
        void onShipUpdate(Ship ship);

        /**
         * Invoked when a client is assigned to a ship.
         */
        void onAssignmentUpdate(ArtemisNetworkInterface iface, int shipIndex);

        /**
         * Invoked when a Console is claimed or released.
         */
        void onClaim(ArtemisNetworkInterface iface, Console console, boolean on);
    }
}
