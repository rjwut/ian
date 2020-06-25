package com.walkertribe.ian.world;

/**
 * No-op implementation of WorldListener. Extend this if you don't want to implement all methods.
 */
public class BaseWorldListener implements WorldListener {
    @Override
    public void onCreate(ArtemisObject obj) {
        // do nothing
    }

    @Override
    public void onPlayerSpawn(ArtemisPlayer player) {
        // do nothing
    }

    @Override
    public void onDelete(ArtemisObject obj) {
        // do nothing
    }
}
