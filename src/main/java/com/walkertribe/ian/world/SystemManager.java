package com.walkertribe.ian.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.protocol.core.eng.EngGridUpdatePacket;
import com.walkertribe.ian.protocol.core.eng.EngGridUpdatePacket.DamconStatus;
import com.walkertribe.ian.protocol.core.eng.EngGridUpdatePacket.GridDamage;
import com.walkertribe.ian.protocol.core.world.DeleteObjectPacket;
import com.walkertribe.ian.protocol.core.world.IntelPacket;
import com.walkertribe.ian.protocol.core.world.ObjectUpdatePacket;
import com.walkertribe.ian.util.GridCoord;
import com.walkertribe.ian.util.ShipSystemGrid;
import com.walkertribe.ian.util.ShipSystemGrid.GridEntry;

/**
 * A repository of Artemis world objects. Register an instance of SystemManager
 * with the ArtemisNetworkInterface and it will keep track of all objects as
 * they're created, updated and destroyed.
 * @author dhleong
 */
public class SystemManager {
    public interface OnObjectCountChangeListener {
        void onObjectCountChanged(int count);
    }
    
    private static final OnObjectCountChangeListener sDummyListener = 
            new OnObjectCountChangeListener() {
        @Override
        public void onObjectCountChanged(int count) {/* nop */}
    };

    private static final boolean DEBUG = false;

    private final HashMap<Integer, ArtemisObject> mObjects = 
            new HashMap<Integer, ArtemisObject>();
    private OnObjectCountChangeListener mListener = sDummyListener;

    private HashMap<GridCoord, Float> mGridDamage;
    private ShipSystemGrid mGrid;
    
    private final HashMap<Integer, DamconStatus> mDamcons =
            new HashMap<Integer, DamconStatus>();
    
    private final ArtemisPlayer[] mPlayers = new ArtemisPlayer[Artemis.SHIP_COUNT];
    
    public SystemManager() {
        clear();
    }
    
    /** Manually add an obj to the system */
    public void addObject(ArtemisObject obj) {
        synchronized(this) {
            mObjects.put(Integer.valueOf(obj.getId()), obj);
        }

        mListener.onObjectCountChanged(mObjects.size());
    }

    @Listener
    public void onPacket(DeleteObjectPacket pkt) {
        synchronized(this) {
            mObjects.remove(Integer.valueOf(pkt.getTarget()));
        }

        // signal change
        if (mObjects.size() == 1) {
            ArtemisObject last = mObjects.values().iterator().next();

            if ("Artemis".equals(last.getName())) {
                // special (hack?) case;
                //  this is actually the end of the game
                clear();
                mListener.onObjectCountChanged(0);
                return;
            }
        } 

        mListener.onObjectCountChanged(mObjects.size());
        return;
    }

    @Listener
    public void onPacket(EngGridUpdatePacket pkt) {
        // this ONLY goes to the appropriate ship's engineer console
        List<GridDamage> damages = pkt.getDamage();

        if (damages.size() > 0 && mGridDamage != null) {
            for (GridDamage d : damages) {
                mGridDamage.put(d.getCoord(), Float.valueOf(d.getDamage()));
            }
        }
        
        // update/init damcon teams
        for (DamconStatus s : pkt.getDamcons()) {
            final Integer team = Integer.valueOf(s.getTeamNumber());

            if (mDamcons.containsKey(team)) {
                DamconStatus old = mDamcons.get(team);
                old.updateFrom(s);
            } else {
                mDamcons.put(team, s);
            }
        }
    }

    @Listener
    public void onPacket(ObjectUpdatePacket pkt) {
        for (ArtemisObject p : pkt.getObjects()) {
            updateOrCreate(p);
        }
    }

    @Listener
    public void onPacket(IntelPacket pkt) {
    	ArtemisObject obj = mObjects.get(Integer.valueOf(pkt.getId()));

    	if (obj != null) {
    		pkt.applyTo(obj);
    	}
    }

    @SuppressWarnings("unused")
    private boolean updateOrCreate(ArtemisObject o) {
    	Integer id = Integer.valueOf(o.getId());
        ArtemisObject p = mObjects.get(id);

        if (p != null) {
            p.updateFrom(o);
            
            if (o instanceof ArtemisPlayer) {
                // just in case we get the ship index AFTER
                //  first creating the object, we store the
                //  updated ORIGINAL with the new ship index
                ArtemisPlayer plr = (ArtemisPlayer) o;

                if (plr.getShipIndex() != -1) {
                    mPlayers[plr.getShipIndex()] = (ArtemisPlayer) p;
                }
            }
            
            return false;
        }

        synchronized(this) {
            mObjects.put(id, o);
        }

        if (o instanceof ArtemisPlayer) {
            ArtemisPlayer plr = (ArtemisPlayer) o;

            if (plr.getShipIndex() >= 0) {
                mPlayers[plr.getShipIndex()] = plr;
            }
        }

        if (DEBUG && o.getName() == null) {
            throw new IllegalStateException("Creating " + p +" without name! " + 
                    Integer.toHexString(o.getId()));
        }

        mListener.onObjectCountChanged(mObjects.size());
        return true;
    }

    public synchronized void getAll(List<ArtemisObject> dest) {
        dest.addAll(mObjects.values());
    }

    public synchronized void getAllSelectable(List<ArtemisObject> dest) {
        for (ArtemisObject obj : mObjects.values()) {
            // tentative
            if (!(obj instanceof ArtemisGenericObject)) {
                dest.add(obj);
            }
        }
    }

    /**
     * Add objects of the given type to the given list 
     */
    public synchronized int getObjects(List<ArtemisObject> dest, ObjectType type) {
        int count = 0;

        for (ArtemisObject obj : mObjects.values()) {
            if (obj.getType() == type) {
                dest.add(obj);
                count++;
            }
        }

        return count;
    }

    /**
     * If you don't want/need to reuse a List, this
     *  will create a list for you
     *  
     * @param type
     * @return
     */
    public List<ArtemisObject> getObjects(ObjectType type) {
        List<ArtemisObject> objs = new ArrayList<ArtemisObject>();
        getObjects(objs, type);
        return objs;
    }

    public ArtemisObject getObject(int objId) {
        return mObjects.get(Integer.valueOf(objId));
    }
    
    /**
     * Get the player ship by index. Ship values range from 0 to 7.
     * @param shipIndex
     * @return
     */
    public ArtemisPlayer getPlayerShip(int shipIndex) {
        if (shipIndex < 0 || shipIndex >= Artemis.SHIP_COUNT) {
            throw new IllegalArgumentException("Invalid ship index: " + shipIndex);
        }
        
        return mPlayers[shipIndex];
    }
    
    /**
     * Get the status of the given Damcon Team
     *  if we have it, else null. It would be
     *  great to have a good first guess, but
     *  I can't seem to find any pattern, nor
     *  does there seem to be an init data---even
     *  the native client just uses a first guess
     *  on connect 
     *  
     * @param teamNumber
     * @return
     */
    public DamconStatus getDamcon(int teamNumber) {
        return mDamcons.get(Integer.valueOf(teamNumber));
    }
    
    public GridEntry getGridAt(int x, int y, int z) {
        return getGridAt(GridCoord.getInstance(x, y, z));
    }

    public GridEntry getGridAt(GridCoord key) {
        if (mGrid == null) {
            throw new IllegalStateException("SystemManager must have a ShipSystemGrid");
        }
        
        return mGrid.getGridAt(key);
    }

    /**
     * Get the type of System at the grid coordinates,
     *  or NULL if it's just a hallway
     * @param x
     * @param y
     * @param z
     * @return
     */
    public ShipSystem getSystemTypeAt(int x, int y, int z) {
        return mGrid.getSystemTypeAt(GridCoord.getInstance(x, y, z));
    }

    /**
     * Get the overall health of the given system
     * @param sys
     * @return A float [0, 1] indicating percentage health
     * @throws IllegalStateException if the SystemManager doesn't
     *  yet have a ShipSystemGrid
     */
    public float getHealthOfSystem(ShipSystem sys) {
        if (mGrid == null) {
            throw new IllegalStateException("SystemManager must have a ShipSystemGrid");
        }
        
        final float total = mGrid.getSystemCount(sys);
        float current = total;

        for (GridCoord c : mGrid.getCoordsFor(sys)) {
            final float damage = getGridDamageAt(c);
        
            if (damage != -1) {
                current -= damage;
            }
        }
        
        return current / total;
    }
    
    /**
     * Get the first object with the given name
     * @param name
     * @return null if no such object or if name is null
     */
    public synchronized ArtemisObject getObjectByName(final String name) {
        if (name == null) {
            return null;
        }
        
        for (ArtemisObject obj : mObjects.values()) {
            if (name.equals(obj.getName())) {
                return obj;
            }
        }
        
        return null;
    }
    
    public boolean hasSystemGrid() {
        return mGrid != null;
    }

    public void setOnObjectCountChangedListener(OnObjectCountChangeListener listener) {
        mListener = (listener == null) ? sDummyListener : listener;
    }
    
    /**
     * Get the damage at a specific grid coord
     * @param coord
     * @return The damage as a value [0, 1] or -1
     *  if we don't have the any entry for the coord
     */
    public float getGridDamageAt(GridCoord coord) {
        if (mGridDamage == null) {
            return -1f;
        }

        Float value = mGridDamage.get(coord);
        return value != null ? value.floatValue() : -1f;
    }

    /**
     * Convenience 
     * @param x
     * @param y
     * @param z
     * @return
     */
    public float getGridDamageAt(int x, int y, int z) {
        return getGridDamageAt(GridCoord.getInstance(x, y, z));
    }

    public Set<Entry<GridCoord, Float>> getGridDamages() {
        return mGridDamage.entrySet();
    }

    /**
     * Set the current ship's (fully loaded) grid
     * @param grid
     */
    public void setSystemGrid(ShipSystemGrid grid) {
        if (mGridDamage == null) {
            mGridDamage = new HashMap<GridCoord, Float>();
        } else {
            mGridDamage.clear(); // just in case
        }
        mGrid = grid;
        
        // fill some default values
        for (GridCoord c : grid.getCoords()) {
            mGridDamage.put(c, Float.valueOf(0f)); // default
        }
    }

    public synchronized void clear() {
        mObjects.clear();
        Arrays.fill(mPlayers, null);
        
        mGrid = null;

        if (mGridDamage != null) {
            mGridDamage.clear();
        }
        
        mDamcons.clear();
    }
}
