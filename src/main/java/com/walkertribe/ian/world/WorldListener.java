package com.walkertribe.ian.world;

/**
 * Interface for objects that want to be notified of World object creation/deletion.
 */
public interface WorldListener {
    /**
     * Invoked whenever an object is created in the World.
     */
    void onCreate(ArtemisObject obj);

    /**
     * Invoked when a player spawns. This is different than onCreate() because the first update for
     * an ArtemisPlayer object that a client receives isn't guaranteed to have the ship index in it.
     * This method will only be invoked when we first receive the object's ship index.
     */
    void onPlayerSpawn(ArtemisPlayer player);

    /**
     * Invoked whenver an object is deleted in the World.
     */
    void onDelete(ArtemisObject obj);
}
