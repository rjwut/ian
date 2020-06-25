package com.walkertribe.ian.world;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.protocol.core.EndGamePacket;
import com.walkertribe.ian.protocol.core.world.BiomechRagePacket;
import com.walkertribe.ian.protocol.core.world.DeleteObjectPacket;
import com.walkertribe.ian.protocol.core.world.IntelPacket;
import com.walkertribe.ian.protocol.core.world.ObjectUpdatePacket;
import com.walkertribe.ian.protocol.core.world.TagPacket;

/**
 * Tracks all game world objects.
 * @author rjwut
 */
public class World implements Iterable<ArtemisObject> {
    private Map<Integer, ArtemisObject> objects = new ConcurrentHashMap<>();
    private Map<Byte, ArtemisPlayer> players = new ConcurrentHashMap<>();
    private List<WorldListener> listeners = new LinkedList<>();
    private int biomechRage;

    /**
     * Registers a new WorldListener.
     */
    public void addListener(WorldListener listener) {
        listeners.add(listener);
    }

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
     * Invoked when an object is tagged.
     */
    @Listener
    public void onTag(TagPacket pkt) {
        int id = pkt.getId();
        Tag tag = pkt.getTag();
        ArtemisObject obj = objects.get(id);

        if (obj != null) {
            // Should always come after the object being tagged
            obj.addTag(tag);
        }
    }

    /**
     * Invoked when the biomech rage level changes.
     */
    @Listener
    public void onBiomechRageChange(BiomechRagePacket pkt) {
        biomechRage = pkt.getRage();
    }

    /**
     * Invoked when an object is removed from the game.
     */
    @Listener
    public void onDelete(DeleteObjectPacket pkt) {
        ArtemisObject obj = objects.remove(pkt.getTarget());

        for (WorldListener listener : listeners) {
            listener.onDelete(obj);
        }
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
        boolean isCreate = false, isPlayerSpawn = false;

        if (obj == null) {
            objects.put(id, update);
            obj = update;
            isCreate = true;
            isPlayerSpawn = obj instanceof ArtemisPlayer && ((ArtemisPlayer) obj).getShipIndex() != Byte.MIN_VALUE;
        } else {
            if (obj instanceof ArtemisPlayer) {
                isPlayerSpawn = ((ArtemisPlayer) obj).getShipIndex() == Byte.MIN_VALUE &&
                        ((ArtemisPlayer) update).getShipIndex() != Byte.MIN_VALUE;
            }

            obj.updateFrom(update);
        }

        if (obj instanceof ArtemisPlayer) {
            ArtemisPlayer player = (ArtemisPlayer) obj;
            byte shipIndex = player.getShipIndex();

            if (player.getShipIndex() >= 0) {
                players.put(shipIndex, player);
            }
        }

        if (isCreate) {
            for (WorldListener listener : listeners) {
                listener.onCreate(obj);
            }
        }

        if (isPlayerSpawn) {
            ArtemisPlayer player = (ArtemisPlayer) obj;

            for (WorldListener listener : listeners) {
                listener.onPlayerSpawn(player);
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
     * Returns a contact (an object that can be contacted via COMMs) by its name. This supports
     * partial name matches against callsigns. If no such object is found, this method returns null.
     */
    public BaseArtemisShielded getContactByName(CharSequence name) {
        String callsign = BaseArtemisShielded.extractCallsign(name);

        for (ArtemisObject obj : objects.values()) {
            String curCallsign = BaseArtemisShielded.extractCallsign(obj.getName());

            if (obj instanceof BaseArtemisShielded && callsign.equals(curCallsign)) {
                return (BaseArtemisShielded) obj;
            }
        }

        return null;
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
     * Returns the biomech rage level.
     */
    public int getBiomechRage() {
        return biomechRage;
    }

    /**
     * Removes all objects from this World.
     */
    public void clear() {
        objects.clear();
        players.clear();
        biomechRage = 0;
    }

    @Override
    public Iterator<ArtemisObject> iterator() {
        return objects.values().iterator();
    }

    /**
     * Returns the objects in this World in the order indicated by the given Comparator.
     */
    public List<ArtemisObject> getAll(Comparator<ArtemisObject> comparator) {
        List<ArtemisObject> sorted = new ArrayList<>(objects.values());
        sorted.sort(comparator);
        return sorted;
    }

    /**
     * Used to filter ArtemisObjects. Replace with the Java 8 native Predicate class when we decide
     * to move to Java 8.
     */
    public static interface Predicate {
        public boolean test(ArtemisObject obj);
    }
}
