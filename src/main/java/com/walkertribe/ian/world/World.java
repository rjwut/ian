package com.walkertribe.ian.world;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.protocol.core.EndGamePacket;
import com.walkertribe.ian.protocol.core.world.DeleteObjectPacket;
import com.walkertribe.ian.protocol.core.world.IntelPacket;
import com.walkertribe.ian.protocol.core.world.ObjectUpdatePacket;

/**
 * Tracks all game world objects.
 * @author rjwut
 */
public class World implements Iterable<ArtemisObject> {
    private Map<Integer, ArtemisObject> objects = new HashMap<>();
    private Map<Byte, ArtemisPlayer> players = new HashMap<>();

    /**
     * Invoked when we get object updates.
     */
    @Listener
    public void onObjectUpdate(ObjectUpdatePacket pkt) {
        for (ArtemisObject obj : pkt.getObjects()) {
            update(obj);
        }
    }

    /**
     * Invoked when we get intel.
     */
    @Listener
    public void onIntel(IntelPacket pkt) {
        int id = pkt.getId();
        ArtemisObject obj = objects.get(id);

        if (obj != null) {
            pkt.applyTo(obj);
        }
    }

    /**
     * Invoked when an object is removed from the game.
     */
    @Listener
    public void onDelete(DeleteObjectPacket pkt) {
        objects.remove(pkt.getTarget());
    }

    /**
     * Invoked when the game's over.
     */
    @Listener
    public void onGameOver(EndGamePacket pkt) {
        clear();
    }

    /**
     * Manually add an object to the World.
     */
    public void update(ArtemisObject update) {
        int id = update.getId();
        ArtemisObject obj = objects.get(id);

        if (obj == null) {
            objects.put(id, update);
        } else {
            obj.updateFrom(update);
        }

        if (obj instanceof ArtemisPlayer) {
            ArtemisPlayer player = (ArtemisPlayer) obj;
            byte shipIndex = player.getShipIndex();

            if (player.getShipIndex() >= 0) {
                players.put(shipIndex, player);
            }
        }
    }

    /**
     * Returns the ArtemisObject with the given ID, or null if no such object exists. 
     */
    public ArtemisObject get(int id) {
        return objects.get(id);
    }

    /**
     * Returns the ArtemisPlayer object with the given ship index, or null if no ship with that
     * index has been observed.
     */
    public ArtemisPlayer getPlayer(byte shipIndex) {
        if (shipIndex < 0 || shipIndex >= Artemis.SHIP_COUNT) {
            throw new IllegalArgumentException("Invalid ship index: " + shipIndex);
        }

        return players.get(shipIndex);
    }

    /**
     * Returns all ArtemisObjects which are instances of the given Class.
     */
    @SuppressWarnings("unchecked")
    public <T extends ArtemisObject> List<T> find(final Class<T> clazz) {
        return (List<T>) find(new Predicate() {
            @Override
            public boolean test(ArtemisObject obj) {
                return clazz.isInstance(obj);
            }
        });
    }

    /**
     * Returns all ArtemisObjects that have the given ObjectType.
     */
    public List<ArtemisObject> find(final ObjectType type) {
        return find(new Predicate() {
            @Override
            public boolean test(ArtemisObject obj) {
                return obj.getType().equals(type);
            }
        });
    }

    /**
     * Returns all ArtemisObjects within the stated range of the given object.
     */
    public List<ArtemisObject> findInRange(final ArtemisObject focusObject, final int range) {
        return find(new Predicate() {
            @Override
            public boolean test(ArtemisObject obj) {
                return obj.distance(focusObject) < range;
            }
            
        });
    }

    /**
     * Same as findInRange(ArtemisObject, int), but ignores the Y-axis in the distance computations.
     */
    public List<ArtemisObject> findInRangeIgnoreY(final ArtemisObject focusObject, final int range) {
        return find(new Predicate() {
            @Override
            public boolean test(ArtemisObject obj) {
                return obj.distanceIgnoreY(focusObject) < range;
            }
            
        });
    }

    /**
     * Returns all objects found within the given rectangle on the X-Z plane.
     */
    public List<ArtemisObject> findInRectangle(final float x0, final float z0, final float x1,
            final float z1) {
        return find(new Predicate() {
            @Override
            public boolean test(ArtemisObject obj) {
                float x = obj.getX();
                float z = obj.getZ();
                return x0 <= x && x <= x1 && z0 <= z && z <= z1;
            }
        });
    }

    /**
     * Returns all ArtemisObjects that match the given Predicate.
     */
    public List<ArtemisObject> find(Predicate predicate) {
        List<ArtemisObject> list = new LinkedList<>();

        for (ArtemisObject obj : objects.values()) {
            if (predicate.test(obj)) {
                list.add(obj);
            }
        }

        return list;
    }

    /**
     * Removes all objects from this World.
     */
    public void clear() {
        objects.clear();
        players.clear();
    }

    @Override
    public Iterator<ArtemisObject> iterator() {
        return objects.values().iterator();
    }

    /**
     * Used to filter ArtemisObjects. Replace with the Java 8 native Predicate class when we decide
     * to move to Java 8.
     */
    public static interface Predicate {
        public boolean test(ArtemisObject obj);
    }
}
