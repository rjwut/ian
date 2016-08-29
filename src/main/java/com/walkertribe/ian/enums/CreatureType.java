package com.walkertribe.ian.enums;

import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.vesseldata.VesselData;

/**
 * The types of creatures. Note: For some reason, wrecks count as creatures.
 * @author rwalker
 */
public enum CreatureType {
	CLASSIC(null),
	WHALE("dat/whale.dxs"),
	SHARK("dat/monster-sha.dxs"),
	DRAGON("dat/monster-drag.dxs"),
	PIRANHA("dat/monster-pira.dxs,dat/monster-pira-jaw.dxs"),
	CHARYBDIS("dat/monster-cone.dxs"),
	NSECT("dat/monster-bug.dxs"),
	WRECK("dat/derelict.dxs");

	private String modelPaths;

	CreatureType(String modelPaths) {
		this.modelPaths = modelPaths;
	}

	/**
     * Returns the Model object for this creature, or null if it has no model.
     * (The "classic" space monster is made of multiple rotating parts and is
     * not a simple model, thus it will return null.) Note that you must invoke
     * VesselData.setArtemisInstallPath() first, before calling this method.
     */
    public Model getModel() {
    	return modelPaths != null ? VesselData.getModel(modelPaths) : null;
    }
}