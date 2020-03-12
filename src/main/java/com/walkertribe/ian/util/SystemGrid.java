package com.walkertribe.ian.util;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.vesseldata.Vessel;
import com.walkertribe.ian.vesseldata.VesselInternals;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * An extension of Grid that is aware of the ship index and Context. This allows us to access
 * information about a grid node's ship (physical) coordinates and the systems located at each one.
 * @author rjwut
 */
public class SystemGrid extends Grid<SystemNode> {
    private byte shipIndex;
    private Context ctx;
    private int shipId = -1;
    VesselInternals internals;

    public SystemGrid(byte shipIndex, Context ctx) {
        this.shipIndex = shipIndex;
        this.ctx = ctx;
    }

    /**
     * Track the player ship so we can get at the VesselInternals.
     */
    @Listener
    public final void onPlayerObjectUpdated(ArtemisPlayer player) {
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
        int hullId = player.getHullId();

        if (hullId != -1) {
            // The hull ID was specified; get the vessel internals
            Vessel vessel = ctx.getVesselData().getVessel(hullId);
            internals = vessel != null ? vessel.getInternals() : null;
        }
    }

    @Override
    public SystemNode newNode(GridCoord coord) {
        return new SystemNode(this, coord);
    }
}
