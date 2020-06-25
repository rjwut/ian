package com.walkertribe.ian.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.model.Point;
import com.walkertribe.ian.protocol.core.eng.DamconTeam;
import com.walkertribe.ian.protocol.core.eng.EngGridUpdatePacket;
import com.walkertribe.ian.vesseldata.Vessel;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * A ship's system Grid. This class can report on infrastructure (physical locations of nodes and
 * what ship systems are located there), current state (damage, status of DAMCON teams), or both.
 * @author rjwut
 */
public class Grid implements Iterable<GridNode> {
    private boolean locked;
    private List<GridNode> nodes;
    private List<DamconTeam> damcons = new ArrayList<>();
    private byte shipIndex;
    private Context ctx;
    private int shipId = -1;
    private int hullId = -1;

    /**
     * Constructor for a Grid with all empty GridNodes and no state data.
     */
    public Grid() {
        initEmptyGrid();
    }

    /**
     * Creates a new Grid that contains a copy of the given Grid's nodes. If clearDamage is false,
     * any damage information stored in the original is copied to the clone, otherwise, the clone
     * will have no damage information. If copyDamcons is true, any information about DAMCON teams
     * is also copied. This constructor is particularly useful to create an unlocked copy of a
     * locked Grid.
     */
    public Grid(Grid original, boolean clearDamage, boolean copyDamcons) {
        copyNodes(original, clearDamage);

        if (copyDamcons) {
            copyDamcons(original);
        }
    }

    /**
     * Creates a new Grid that will be populated from received server packets. Until it gets
     * populated, the nodes will contain only empty GridNodes and no state.
     */
    public Grid(byte shipIndex, Context ctx) {
        this();
        this.shipIndex = shipIndex;
        this.ctx = ctx;
    }

    /**
     * Watch for the player ship's hull ID to be reported so that we can populate the Grid with
     * infrastructure data.
     */
    @Listener
    public void onPlayerObjectUpdated(ArtemisPlayer player) {
        if (shipId == -1) {
            // We don't know the ship's ID yet
            byte curIndex = player.getShipIndex();

            if (curIndex == -1 || curIndex != shipIndex) {
                return; // this isn't the one we want
            }

            // We found it; record the ID
            shipId = player.getId();
        } else {
            // We know the ID, so just check for that
            if (player.getId() != shipId) {
                return; // this isn't the one we want
            }
        }

        // If we got here, this is the ship we want
        if (hullId == -1) {
            hullId = player.getHullId();

            if (hullId != -1 && ctx != null) {
                // The hull ID was specified; get the infrastructure
                Vessel vessel = ctx.getVesselData().getVessel(hullId);

                if (vessel != null) {
                    synchronized (this) {
                        copyNodes(vessel.getGrid(), false);
                    }
                }
            }
        }
    }

    /**
     * Updates the Grid to reflect damage reported by EngGridUpdatePackets.
     */
    @Listener
    public void onUpdate(EngGridUpdatePacket pkt) {
        synchronized (this) {
            if (pkt.isFullUpdate()) {
                for (GridNode node : nodes) {
                    node.setDamage(0);
                }
            }

            for (GridNode dmg : pkt.getDamage()) {
                GridCoord coord = dmg.getCoord();
                GridNode node = getNode(coord);

                if (node == null) {
                    node = new GridNode(this, coord);
                    setNode(node);
                }

                node.setDamage(dmg.getDamage());
            }

            for (DamconTeam team : pkt.getDamcons()) {
                byte id = team.getId();

                while (damcons.size() <= id) {
                    damcons.add(null);
                }

                damcons.set(id, team);
            }
        }
    }

    /**
     * Returns the GridNode at the given nodes coordinates. This method will never return null.
     */
    public GridNode getNode(GridCoord coord) {
        synchronized (this) {
            return nodes.get(coord.index());
        }
    }

    /**
     * Sets a GridNode in the nodes. This will throw an IllegalArgumentException if the given
     * GridNode is null and an IllegalStateException if the Grid is locked.
     */
    public void setNode(GridNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Can't store a null GridNode");
        }

        synchronized (this) {
            assertUnlocked();
            int index = node.getCoord().index();

            while (nodes.size() <= index) {
                nodes.add(null);
            }

            nodes.set(node.getCoord().index(), node);
        }
    }

    /**
     * Returns the DamconTeam with the given ID, or null if no team exists with that ID.
     */
    public DamconTeam getDamcon(byte id) {
        synchronized (this) {
            return id >= damcons.size() ? null : damcons.get(id);
        }
    }

    /**
     * Updates the Grid with the given DamconTeam.
     */
    public void setDamcon(DamconTeam team) {
        synchronized (this) {
            assertUnlocked();
            damcons.set(team.getId(), team);
        }
    }

    @Override
    public Iterator<GridNode> iterator() {
        return nodes.iterator();
    }

    /**
     * Returns a new List containing all this Grid's GridNodes.
     */
    public List<GridNode> getAllNodes() {
        return new ArrayList<>(nodes);
    }

    /**
     * Returns a List containing only the accessible GridNodes from this Grid.
     */
    public List<GridNode> getAccessibleNodes() {
        List<GridNode> accessible = new ArrayList<>();

        for (GridNode node : nodes) {
            if (node.isAccessible()) {
                accessible.add(node);
            }
        }

        return accessible;
    }

    /**
     * Returns a new List containing all this Grid's DAMCON teams.
     */
    public List<DamconTeam> getAllDamconTeams() {
        return new ArrayList<>(damcons);
    }

    /**
     * Returns a Map that groups SystemNodes by ShipSystem.
     */
    public Map<ShipSystem, List<GridNode>> groupNodesBySystem() {
        Map<ShipSystem, List<GridNode>> map = new LinkedHashMap<>();

        for (ShipSystem sys : ShipSystem.values()) {
            map.put(sys, new LinkedList<GridNode>());
        }

        for (GridNode node : nodes) {
            ShipSystem sys = node.getSystem();

            if (sys != null) {
                map.get(sys).add(node);
            }
        }

        return map;
    }

    /**
     * <p>
     * Returns a Map of the Point objects showing the location of the objects storied in this Grid
     * on the ship's physical coordinate space. Each Point is keyed by a String as follows:
     * </p>
     * <ul>
     * <li>Nodes: "[x,y,z]" (using system grid coordinates)</li>
     * <li>DAMCON teams: "DAMCON #" (where # is the DAMCON team ID)</li>
     * </ul>
     */
    public Map<String, Point> toPointCloud() {
        Map<String, Point> map = new HashMap<>();

        for (GridNode node : nodes) {
            if (node.isAccessible()) {
                map.put(node.getCoord().toString(), node.getPoint());
            }
        }

        for (DamconTeam team : damcons) {
            if (team != null) {
                GridCoord locationCoord = team.getLocation();
                GridCoord goalCoord = team.getGoal();
                Point point = getNode(goalCoord).getPoint();

                if (locationCoord != goalCoord) {
                    point = point.getBetween(getNode(locationCoord).getPoint(), team.getProgress());
                }

                map.put("DAMCON " + team.getId(), point);
            }
        }

        return map;
    }

    /**
     * Resets the current Grid to have no data.
     */
    public void clear() {
        synchronized (this) {
            assertUnlocked();
            shipId = -1;
            hullId = -1;
            initEmptyGrid();
            damcons.clear();
        }
    }

    /**
     * Make this Grid read-only. Once a Grid is locked, any attempt to modify it will throw an
     * IllegalStateException.
     */
    public void lock() {
        synchronized (this) {
            locked = true;
        }
    }

    /**
     * Throws an IllegalStateException if this Grid is locked.
     */
    public void assertUnlocked() {
        if (locked) {
            throw new IllegalStateException("Grid is locked");
        }
    }

    /**
     * Populates the Grid with a new set of empty GridNodes.
     */
    private void initEmptyGrid() {
        nodes = new ArrayList<>(GridCoord.LENGTH);

        for (Iterator<GridCoord> iter = GridCoord.iterator(); iter.hasNext(); ) {
            nodes.add(new GridNode(this, iter.next()));
        }
    }

    /**
     * Populates this Grid with a copy of the given Grid's nodes. If clearDamage is false, any
     * existing damage values in this Grid will be retained; otherwise, they'll be cleared.
     */
    private void copyNodes(Grid original, boolean clearDamage) {
        boolean wasPopulated = nodes != null;

        if (!wasPopulated) {
            nodes = new ArrayList<GridNode>();
        }

        for (GridNode node : original) {
            GridNode newNode = new GridNode(this, node);

            if (!clearDamage && wasPopulated) {
                GridNode oldNode = nodes.get(node.coord.index);

                if (oldNode != null) {
                    float damage = oldNode.getDamage();

                    if (damage >= 0) {
                        newNode.setDamage(damage);
                    }
                }
            }

            setNode(newNode);
        }
    }

    /**
     * Copies the DAMCON teams from the given Grid to this one.
     */
    private void copyDamcons(Grid original) {
        synchronized (this) {
            assertUnlocked();

            for (DamconTeam team : original.getAllDamconTeams()) {
                damcons.set(team.getId(), team);
            }
        }
    }
}
